package com.woosan.hr_system.report.controller;

import com.woosan.hr_system.report.model.Report;
import com.woosan.hr_system.report.model.ReportRequest;
import com.woosan.hr_system.report.service.ReportRequestService;
import com.woosan.hr_system.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.woosan.hr_system.report.model.FileMetadata;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private ReportRequestService reportRequestService;

    @GetMapping("/report-home") // report-home 페이지 이동
    public String reportHome() {
        return "report/report-home";
    }

    @GetMapping("/get-all-reports") // 모든 보고서 목록 조회
    @ResponseBody
    public List<Report> getAllReports() {
        return reportService.getAllReports();
    }

    @GetMapping("/get-all-report-requests") // 모든 보고서 요청 목록 조회
    @ResponseBody
    public List<ReportRequest> getAllReportRequests() {
        return reportRequestService.getAllReportRequests();
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

    @GetMapping("/request/{requestId}") // 특정 요청 조회
    public String viewRequest(@PathVariable("requestId") Long requestId, Model model) {
        System.out.println("컨트롤러");
        ReportRequest request = reportRequestService.getRequestById(requestId);
        model.addAttribute("request", request);
        return "report/request/request-view";
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
                               @RequestParam("completeDate") String completeDate,
                               @RequestParam("file") MultipartFile file,
                               Model model) {
        try {
            // completeDate를 Date로 변환
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(completeDate, formatter);
            Date completeDateSql = Date.valueOf(localDate);

            reportService.createReport(title, content, approverId, completeDateSql, file);

            model.addAttribute("message", "보고서 작성 완료");
            return "redirect:/report/report-home";
        } catch (DateTimeParseException e) {
            model.addAttribute("message", "날짜 형식이 잘못되었습니다.");
            return "report/write";
        } catch (IOException e) {
            model.addAttribute("message", "파일 업로드 실패");
            e.printStackTrace();
            return "/write";
        }
    }

    @GetMapping("/edit") // 수정 페이지 이동
    public String editReport(@RequestParam("reportId") Long reportId, Model model) {
        Report report = reportService.getReportById(reportId);
        model.addAttribute("report", report);
        return "report/edit";
    }

    @PostMapping("/update") // 보고서 수정
    public String updateReport(@RequestParam("reportId") Long reportId,
                               @RequestParam("title") String title,
                               @RequestParam("content") String content,
                               @RequestParam("completeDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate completeDate) {
        reportService.updateReport(reportId, title, content, completeDate);
        return "redirect:/report/" + reportId;
    }

    @PostMapping("/approve")
    public String approveReport(@RequestParam("reportId") Long reportId,
                                @RequestParam("status") String status,
                                @RequestParam(name = "rejectionReason", required = false) String rejectionReason) {
        try {
            reportService.updateApprovalStatus(reportId, status, rejectionReason);
            return "redirect:/report/report-home"; // 변경 후 다시 보고서 보기 페이지로 리다이렉트
        } catch (Exception e) {
            return "error"; // 에러 페이지로 리다이렉트하거나 에러 메시지 표시
        }
    }
}
