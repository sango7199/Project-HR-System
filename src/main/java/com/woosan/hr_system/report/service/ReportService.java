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

    void createReport(Report report, MultipartFile file) throws IOException;

    void updateReport(Report report);
    void updateApprovalStatus(Report report);

    void deleteReport(Long id);
    List<FileMetadata> uploadFiles(Long reportId, MultipartFile[] files) throws IOException;


}
