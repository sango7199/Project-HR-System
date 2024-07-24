package com.woosan.hr_system.report.service;

import com.woosan.hr_system.report.model.ReportStat;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import com.woosan.hr_system.report.model.Report;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import com.woosan.hr_system.report.model.FileMetadata;

public interface ReportService {
    // 조회 관련 메소드
    List<Report> getAllReports(); // 모든 리포트 조회
    Report getReportById(Long reportId); // 특정 리포트 조회
    FileMetadata getReportFileById(Long fileId); // 파일 조회
    List<Report> getPendingApprovalReports(); // 결재할 보고서 목록 조회

    // 생성 관련 메소드
    void createReport(Report report, MultipartFile file) throws IOException;
    List<FileMetadata> uploadFiles(Long reportId, MultipartFile[] files) throws IOException;

    // 보고서 수정 관련 메소드
    void updateReport(Report report);
    void updateApprovalStatus(Report report);

    // 보고서 삭제
    void deleteReport(Long id);

    List<ReportStat> getReportStats(String startDate, String endDate);

}
