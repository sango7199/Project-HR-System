package com.woosan.hr_system.resignation.controller;

import com.woosan.hr_system.auth.aspect.RequireHRPermission;
import com.woosan.hr_system.employee.service.EmployeeService;
import com.woosan.hr_system.resignation.model.Resignation;
import com.woosan.hr_system.resignation.service.ResignationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/employee")
public class ResignationApiController {

    @Autowired
    private ResignationService resignationService;
    @Autowired
    private EmployeeService employeeService;

    // 사원 퇴사 처리
    @RequireHRPermission
    @PostMapping(value = "/resign/{employeeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> resignEmployee(@PathVariable("employeeId") String employeeId,
                                                 @RequestPart("resignation") Resignation resignation,
                                                 @RequestPart(value = "resignationDocuments", required = false) MultipartFile[] resignationDocuments) {
        // 파일들 체크 후 DB에 저장할 파일명 반환
//        validateFilesAndGetFileName(resignation, resignationDocuments);

        //    private void validateFilesAndGetFileName(Resignation resignation, MultipartFile[] resignationDocuments) {
//        if (resignationDocuments != null) {
//            employeeService.updateResignationDocuments(resignation, fileService.checkAndUploadFiles(resignationDocuments));
//        }
//    }
        // 퇴사 문서 파일 업로드
        if (resignationDocuments != null && resignationDocuments.length > 0) {
//            resignationService.uploadResignationDocuments(resignationDocuments);
        }

        // 사원 퇴사 처리
        resignationService.resignEmployee(employeeId, resignation);

        // 재직 상태 - 퇴사 처리
        employeeService.updateStatus(employeeId, "퇴사");

        String name = employeeService.getEmployeeNameById(employeeId);
        return ResponseEntity.ok("'" + name + "' 사원이 퇴사 처리되었습니다.");
    }

    // 사원 퇴사 정보 수정
    @RequireHRPermission
    @PutMapping(value = "/update/resignation/{employeeId}", consumes = "multipart/form-data")
    public ResponseEntity<String> updateResignationInfo(@PathVariable("employeeId") String employeeId,
                                                        @RequestPart("resignation") Resignation resignation,
                                                        @RequestPart(value = "resignationDocuments", required = false) MultipartFile[] resignationDocuments) {
        // 파일들 체크 후 DB에 저장할 파일명 반환
//        validateFilesAndGetFileName(resignation, resignationDocuments);

        // 퇴사 정보 수정
        resignationService.updateResignationInfo(employeeId, resignation);

        String name = employeeService.getEmployeeNameById(employeeId);
        return ResponseEntity.ok("'" + name + "' 사원의 퇴사 정보가 수정되었습니다.");
    }


}
