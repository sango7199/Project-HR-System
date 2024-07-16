package com.woosan.hr_system.report.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import com.woosan.hr_system.report.model.Report;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import com.woosan.hr_system.report.model.FileMetadata;

public interface ReportService {
    List<Report> getAllReports();
    Report getReportById(Long reportId);
    FileMetadata getReportFileById(Long fileId);
    void createReport(String title, String content, String approverId, Date completeDate, MultipartFile file) throws IOException;
    void deleteReport(Long reportId);
    List<FileMetadata> uploadFiles(Long reportId, MultipartFile[] files) throws IOException;

    void updateReport(Long id, String title, String content, LocalDate CompleteDate);
}
