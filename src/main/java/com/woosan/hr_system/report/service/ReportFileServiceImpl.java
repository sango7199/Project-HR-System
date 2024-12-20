package com.woosan.hr_system.report.service;

import com.woosan.hr_system.file.model.File;
import com.woosan.hr_system.file.service.FileService;
import com.woosan.hr_system.report.dao.ReportFileDAO;
import com.woosan.hr_system.report.model.Report;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ReportFileServiceImpl implements ReportFileService {
    @Autowired
    private ReportFileDAO reportFileDAO;
    @Autowired
    private FileService fileService;

    @Override // 보고서 파일 생성
    public void createReportFile(int reportId, int fileId) {
        reportFileDAO.createReportFile(reportId, fileId);
    }

    @Override // reportId를 이용한 보고서 조회
    public List<Integer> getFileIdsByReportId(int reportId) {
        return reportFileDAO.getFileIdsByReportId(reportId);
    }

    @Override // fileId를 이용한 보고서 조회
    public List<Integer> getReportIdsByFileId(int fileId) {
        return reportFileDAO.getReportIdsByFileId(fileId);
    }

    @Override // 파일 수정
    public void updateReportFile(Report report, List<MultipartFile> toUploadFileList, List<Integer> userSelectedFileIdList, List<Integer> existingFileIdList, List<Integer> createdReportIdList) {
        // 조인테이블에 연결할 fileIdList
        List<Integer> newFileIdList = new ArrayList<>();

        boolean hasUpdated = toUploadFileList == null || toUploadFileList.isEmpty(); // null이거나 비어있으면 true
        boolean hasOriginal = userSelectedFileIdList == null || userSelectedFileIdList.isEmpty(); // null이거나 비어있으면 true

        if (hasOriginal) {
            for (Integer fileId : existingFileIdList) {
                deleteReportFile(report.getReportId(), fileId);
            }
        }
        if (!hasOriginal) {
            handleRegisteredFile(userSelectedFileIdList, existingFileIdList, report.getReportId());
            // newFileIdList에 userSelectedfileIdList 요소넣기
            newFileIdList.addAll(userSelectedFileIdList);
        }
        if (!hasUpdated) {
            List<Integer> uploadedFileIdList = handleUploadFile(toUploadFileList);
            // newFileIdList에 uploadedFileIdList 요소 넣기
            newFileIdList.addAll(uploadedFileIdList);
        }

        insertJoinTable(createdReportIdList, newFileIdList);
    }

    // 기존의 파일 중 사용자가 삭제한 파일이 있는지 확인함
    private void handleRegisteredFile(List<Integer> userSelectedFileIdList, List<Integer> existingFileIdList, int reportId) {
        

        // existingFileIdList 중에서 userSelectedFileIdList에 없는 파일을 삭제
        existingFileIdList.stream()
                .filter(fileId -> !userSelectedFileIdList.contains(fileId))
                .forEach(fileId -> deleteReportFile(reportId, fileId));
    }

    private List<Integer> handleUploadFile(List<MultipartFile> toUploadFileList) {
        List<Integer> fileIds = new ArrayList<>();
        for (MultipartFile file : toUploadFileList) {
            int fileId = fileService.uploadingFile(file, "report");
            fileIds.add(fileId);
        }
        return fileIds;
    }

    // fileId와 reportId를 받아서 조인테이블 데이터 생성
    private void insertJoinTable(List<Integer> createdReportIdList, List<Integer> fileIdList) {
        // fileIdList가 null인 경우 메서드를 종료
        if (fileIdList == null) {
            return;
        }


        List<Integer> jointableReportIdList = new ArrayList<>(createdReportIdList);
        List<Integer> jointableFileIdList = new ArrayList<>(fileIdList);

        for (Integer reportId : jointableReportIdList) {
            List<Integer> existingFileIdList = getFileIdsByReportId(reportId);

            // 이미 존재하는 파일 ID를 제외한 파일만 추가
            jointableFileIdList.stream()
                    .filter(fileId -> !existingFileIdList.contains(fileId))
                    .forEach(fileId -> createReportFile(reportId, fileId));
        }
    }

    @Override // 보고서 수정 - reportId로 파일 삭제
    public void deleteReportFileByReportId(int reportId) {
        // 조인 테이블에서 reportId에 해당하는 fileId 리스트를 가져옴
        List<Integer> fileIdList = reportFileDAO.getFileIdsByReportId(reportId);

        for (Integer fileId : fileIdList) {
            // report의 file 개수만큼 조인테이블 삭제
            deleteReportFile(reportId, fileId);
        }
    }

    @Override
    // 파일 삭제
    public void deleteReportFile(int reportId, int fileId) {

        List<Integer> reportIdList = reportFileDAO.getReportIdsByFileId(fileId);

        // 조인 테이블에서 파일Id와 reportId에 해당하는 레코드를 삭제
        reportFileDAO.deleteReportFile(reportId, fileId);

        // 다른 report가 파일을 참조하고 있지 않으면 파일까지 삭제
        if (reportIdList.size() == 1) {
            // 파일 정보를 가져옴
            File file = fileService.getFileInfo(fileId);
            // 파일아카이브 삽입
            reportFileDAO.createReportFileArchive(file, reportId);
            // 파일 삭제
            fileService.deleteFile(fileId);
        }
    }

}
