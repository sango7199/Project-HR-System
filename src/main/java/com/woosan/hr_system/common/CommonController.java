package com.woosan.hr_system.common;

import com.woosan.hr_system.auth.model.Password;
import com.woosan.hr_system.auth.service.AuthService;
import com.woosan.hr_system.employee.model.Employee;
import com.woosan.hr_system.employee.service.EmployeeService;
import com.woosan.hr_system.file.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping
public class CommonController {
    @Autowired
    private AuthService authService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private FileService fileService;

    @GetMapping("home") // 홈 화면으로 이동
    public String home(Model model) {
        String result = authService.isPasswordChangeRequired();
        switch (result) {
            case "FirstChangeRequired" -> model.addAttribute("message", "FirstChangeRequired");
            case "ChangeRequired" -> model.addAttribute("message", "ChangeRequired");
            default -> model.addAttribute("message", "NoChangeRequired");
        }
        return "common/home";
    }

    @GetMapping("my") // 내 정보 조회
    public String viewMyInfo(Model model) {
        String employeeId = authService.getAuthenticatedUser().getUsername();
        // 내 정보 조회
        Employee employee = employeeService.getEmployeeDetails(employeeId);
        model.addAttribute("employee", employee);
        // 비밀번호 정보 조회
        Password password = authService.getPasswordInfoById(employeeId);
        model.addAttribute("password", password);

        model.addAttribute("pictureUrl", fileService.getUrl(employee.getPicture()));
        return "common/my";
    }

    @GetMapping("{employeeId}/edit") // 내 정보 수정 페이지 이동
    public String viewMyInfoEditForm(@PathVariable("employeeId") String employeeId, Model model) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        model.addAttribute("employee", employee);

        // 파일 관련 정보 모델에 추가
        int fileId = employee.getPicture();
        model.addAttribute("pictureUrl", fileService.getUrl(fileId));
        model.addAttribute("originalFileName", fileService.getFileInfo(fileId).getOriginalFileName());

        return "common/my-edit";
    }
}
