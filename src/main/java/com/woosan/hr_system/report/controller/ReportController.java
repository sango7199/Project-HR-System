package com.woosan.hr_system.report.controller;

import com.woosan.hr_system.report.model.Report;
import com.woosan.hr_system.report.model.Request;
import com.woosan.hr_system.report.service.RequestService;
import com.woosan.hr_system.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.woosan.hr_system.report.model.FileMetadata;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private RequestService requestService;

    @GetMapping("/main") // main 페이지 이동
    public String getList(Model model) {
        List<Report> reports = reportService.getAllReports();
        List<Request> requests = requestService.getAllRequests();
        model.addAttribute("reports", reports);
        model.addAttribute("requests", requests);
        return "report/main"; // main.html을 반환
    }

    @GetMapping("/{reportId}") // 특정 보고서 조회
    public String viewReport(@PathVariable("reportId") Long reportId, Model model) {
        Report report = reportService.getReportById(reportId);
        model.addAttribute("report", report);

        if (report.getFileId() != null) {
            FileMetadata reportFile = reportService.getReportFileById(report.getFileId());
            model.addAttribute("reportFile", reportFile);
        }
        return "report/view";
    }

    @GetMapping("/write") // 보고서 작성 페이지 이동
    public String showCreateForm(Model model) {
        model.addAttribute("report", new Report());
        return "report/write";  // write.html로 연결
    }

    @PostMapping("/write") // 보고서 생성
    public String createReport(@RequestParam("title") String title,
                               @RequestParam("content") String content,
                               @RequestParam("approverId") String approverId,
                               @RequestParam("completeDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate completeDate,
                               @RequestParam("file") MultipartFile file,
                               Model model) {
        try {
            Report report = new Report();
            LocalDateTime createdDate = LocalDateTime.now(); // 현재 기준 생성시간 설정

            report.setTitle(title);
            report.setContent(content);
            report.setApproverId(approverId);
            report.setCompleteDate(completeDate);
            report.setCreatedDate(createdDate);
            report.setStatus("미처리"); // 기본 결재 상태 설정

            reportService.createReport(report, file);

            model.addAttribute("message", "보고서 작성 완료");
            return "redirect:/report/main";
        } catch (DateTimeParseException e) {
            model.addAttribute("message", "날짜 형식이 잘못되었습니다.");
            return "report/write";
        } catch (IOException e) {
            model.addAttribute("message", "파일 업로드 실패");
            e.printStackTrace();
            return "report/write";
        }
    }

    @GetMapping("/edit") // 수정 페이지 이동
    public String editReport(@RequestParam("reportId") Long reportId, Model model) {
        Report report = reportService.getReportById(reportId);
        model.addAttribute("report", report);
        return "report/edit";
    }

    @PostMapping("/edit") // 보고서 수정
    public String updateReport(@RequestParam("reportId") Long reportId,
                               @RequestParam("title") String title,
                               @RequestParam("content") String content,
                               @RequestParam("completeDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate completeDate) {

        Report report = new Report();
        LocalDateTime modified_date = LocalDateTime.now(); // 현재 기준 수정시간 설정

        report.setReportId(reportId);
        report.setTitle(title);
        report.setContent(content);
        report.setCompleteDate(completeDate);
        report.setModifiedDate(modified_date);

        reportService.updateReport(report);
        return "redirect:/report/" + reportId;
    }

    @PostMapping("/approve") // 결재 처리
    public String approveReport(@RequestParam("reportId") Long reportId,
                                @RequestParam("status") String status,
                                @RequestParam(name = "rejectionReason", required = false) String rejectionReason) {
        try {
            Report report = new Report();
            report.setReportId(reportId);
            report.setStatus(status);
            report.setRejectReason(rejectionReason);

            reportService.updateApprovalStatus(report);
            return "redirect:/report/main";
        } catch (Exception e) {
            return "error"; // 에러 메시지 표시
        }
    }

    @DeleteMapping("/delete/{reportId}") // 보고서 삭제
    public String deleteReport(@PathVariable("reportId") Long id, RedirectAttributes redirectAttributes) {
        reportService.deleteReport(id);
        return "redirect:/report/main";
    }
}
