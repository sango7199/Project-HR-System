package com.woosan.hr_system.report.service;

import com.woosan.hr_system.report.dao.ReportDAO;
import com.woosan.hr_system.report.model.FileMetadata;
import com.woosan.hr_system.report.model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportDAO reportDAO;

    @Override
    public List<Report> getAllReports() {
        return reportDAO.getAllReports();
    }

    @Override
    public Report getReportById(Long reportId) {
        return reportDAO.getReportById(reportId);
    }

    @Override
    public FileMetadata getReportFileById(Long fileId) {
        return reportDAO.getReportFileById(fileId);
    }

    @Override
    public void createReport(String title, String content, String approverId, Date completeDate, MultipartFile file) throws IOException {
        // 보고서 생성 및 저장
        Report report = new Report();
        report.setTitle(title);
        report.setContent(content);
        report.setApproverId(approverId);
        report.setCompleteDate(completeDate);
        report.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        report.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        report.setStatus("미처리"); // 기본 상태 설정

        reportDAO.insertReport(report);

        // 파일 업로드
        if (!file.isEmpty()) {
            reportDAO.uploadFiles(report.getReportId(), new MultipartFile[]{file});
        }
    }

    @Override
    public void updateReport(Long id, String title, String content, LocalDate completeDate) {
        Report report = reportDAO.getReportById(id);
        if (title != null) {
            report.setTitle(title);
        }
        if (content != null) {
            report.setContent(content);
        }
        if (completeDate != null) {
            report.setCompleteDate(Date.valueOf(completeDate));
        }
        reportDAO.updateReport(report);
    }


    @Override
    public void deleteReport(Long reportId) {
        reportDAO.deleteReport(reportId);
    }

    @Override
    public List<FileMetadata> uploadFiles(Long reportId, MultipartFile[] files) throws IOException {
        List<FileMetadata> uploadedFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                String uuidFilename = UUID.randomUUID().toString();
                String filePath = saveFile(file, uuidFilename);
                FileMetadata fileMetadata = saveMetadata(reportId, originalFilename, uuidFilename, filePath, file.getSize());
                uploadedFiles.add(fileMetadata);
            }
        }
        return uploadedFiles;
    }

    private String saveFile(MultipartFile file, String uuidFilename) throws IOException {
        String uploadDir = "/path/to/upload";
        File dest = new File(uploadDir + "/" + uuidFilename);
        file.transferTo(dest);
        return dest.getAbsolutePath();
    }

    private FileMetadata saveMetadata(Long reportId, String originalFilename, String uuidFilename, String filePath, long size) {
        FileMetadata metadata = new FileMetadata();
        metadata.setReportId(reportId);
        metadata.setCloudServerFileUrl("Cloud URL"); // 클라우드 URL 추후 설정
        metadata.setOriginalFilename(originalFilename);
        metadata.setUuidFilename(uuidFilename);
        metadata.setSize(size);
        metadata.setUploadDate(new java.util.Date());

        reportDAO.insertFileMetadata(metadata);

        return metadata;
    }
}
