package com.woosan.hr_system.report.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import com.woosan.hr_system.report.model.Report;
import java.util.List;
import java.util.Map;
import com.woosan.hr_system.report.model.FileMetadata;

public interface ReportService {
    List<Report> getAllReports();
    Report getReportById(Long reportId);
    void insertReport(Report report);
    void updateReport(Report report);
    void updateReportPartial(Long reportId, Map<String, Object> updates);
    void deleteReport(Long reportId);
    List<FileMetadata> uploadFiles(Long reportId, MultipartFile[] files) throws IOException;
}
