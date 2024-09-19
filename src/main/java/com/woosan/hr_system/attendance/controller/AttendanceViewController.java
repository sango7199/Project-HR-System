package com.woosan.hr_system.attendance.controller;

import com.woosan.hr_system.attendance.model.Attendance;
import com.woosan.hr_system.attendance.service.AttendanceService;
import com.woosan.hr_system.auth.service.AuthService;
import com.woosan.hr_system.employee.service.EmployeeService;
import com.woosan.hr_system.vacation.model.Vacation;
import com.woosan.hr_system.vacation.service.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/attendance")
public class AttendanceViewController {
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private AuthService authService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private VacationService vacationService;

    @GetMapping// 나의 근태 페이지
    public String viewMyAttendance(Model model) {
        // 로그인 사원 ID 조회
        String employeeId = authService.getAuthenticatedUser().getUsername();
        model.addAttribute("attendanceList", attendanceService.getAttendanceByEmployeeId(employeeId));
        return "attendance/my-log";
    }

    @GetMapping("/{attendanceId}") // 근태 상세 페이지
    public String viewAttendanceDetail(@PathVariable("attendanceId") int attendanceId, Model model) {
        // 근태 정보 상세 조회
        Attendance attendance = attendanceService.getAttendanceById(attendanceId);
        model.addAttribute(attendance);

        // 사원 정보 상세 조회 후 모델에 추가
        model.addAttribute("employee", employeeService.getEmployeeById(attendance.getEmployeeId()));
        return "attendance/detail";
    }

    @GetMapping("/commute") // 출퇴근 페이지
    public String viewCommuteModal(Model model) {
        // 로그인한 사원의 금일 근태기록 있는지 확인 후 모델에 추가
        model.addAttribute("attendance", attendanceService.hasTodayAttendanceRecord());
        return "attendance/commute";
    }

    @GetMapping("/early-leave") // 조퇴 페이지
    public String viewEarlyLeaveModal(Model model) {
        // 로그인한 사원의 금일 근태기록 있는지 확인 후 모델에 추가
        model.addAttribute("attendance", attendanceService.hasTodayAttendanceRecord());
        return "attendance/early-leave";
    }

    @GetMapping("/{attendanceId}/edit") // 근태 수정 페이지
    public String viewEditAttendanceForm(@PathVariable("attendanceId") int attendanceId, Model model) {
        // 근태 정보 상세 조회
        Attendance attendance = attendanceService.getAttendanceById(attendanceId);
        model.addAttribute(attendance);

        String employeeId = attendance.getEmployeeId();

        // 사원 정보 상세 조회 후 모델에 추가
        model.addAttribute("employee", employeeService.getEmployeeById(employeeId));

        // 사원의 휴가 정보 조회 후 승인된 휴가 정보만 모델에 추가
        List<Vacation> vacationList = vacationService.getVacationByEmployeeId(employeeId).stream()
                .filter(vacation -> vacation.getApprovalStatus().equals("승인"))
                .toList();

        model.addAttribute("vacationList", vacationList);
        return "attendance/edit";
    }
}
