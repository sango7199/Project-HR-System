package com.woosan.hr_system.upload.service;

import com.woosan.hr_system.auth.aspect.RequireManagerPermission;
import com.woosan.hr_system.auth.model.UserSessionInfo;
import com.woosan.hr_system.exception.file.FileBadRequestException;
import com.woosan.hr_system.exception.file.FileInfoNotFoundException;
import com.woosan.hr_system.exception.file.FileProcessingException;
import com.woosan.hr_system.upload.dao.FileDAO;
import com.woosan.hr_system.upload.model.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private FileDAO fileDAO;

    private static final String S3_URL_PREFIX = "https://haruharu-hrsystem-bucket.s3.amazonaws.com/";
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    // ====================================================== 조회 ======================================================
    @Override // 파일의 S3 주소 반환
    public String getUrl(int fileId) {
        return S3_URL_PREFIX + fileDAO.getFileStoredNameById(fileId);
    }

    @Override // 파일 정보 조회
    public File getFileInfo(int fileId) {
        return findFileById(fileId);
    }

    // 파일 정보 조회 후 null 확인
    private File findFileById(int fileId) {
        File fileInfo = fileDAO.getFileById(fileId);
        if (fileInfo == null) throw new FileInfoNotFoundException(fileId);
        return fileInfo;
    }

    @Override // 파일 리스트 정보 조회
    public List<File> getFileListById(List<Integer> fileIdList) {
        return fileDAO.getFileListById(fileIdList);
    }
    // ====================================================== 조회 ======================================================

    // ===================================================== 업로드 =====================================================
    // 파일 확인 후 업로드
    @Transactional
    @Override
    public int uploadingFile(MultipartFile file, String fileIdUsage) {
        // 파일 검증
        validateFile(file);

        // 파일 업로드
        String storedFileName = uploadToS3(file);

        log.info("Uploading file " + storedFileName + " to S3");

        return saveFileTransactionally(file, storedFileName, fileIdUsage);
    }

    // 트랜잭션 내부에서 파일 정보 저장
    @Transactional
    protected int saveFileTransactionally(MultipartFile file, String storedFileName, String fileIdUsage) {
        // 파일 정보 생성
        File fileInfo = createFileInfo(file, storedFileName, fileIdUsage);

        // 트랜잭션 리스너 등록
        registerRollbackHandler(storedFileName);

        // DB에 파일 정보 입력
        fileDAO.insertFile(fileInfo);

        // fileInfo 객체의 ID를 반환
        return fileInfo.getFileId();
    }

    // 파일 정보 생성
    private File createFileInfo(MultipartFile file, String storedFileName, String fileIdUsage) {
        UserSessionInfo userSessionInfo = new UserSessionInfo();

        return new File(
                file.getOriginalFilename(),
                storedFileName,
                file.getSize(),
                userSessionInfo.getNow(),
                userSessionInfo.getCurrentEmployeeId(),
                fileIdUsage
        );
    }

    @Transactional
    // 트랜잭션 리스너 등록
    protected void registerRollbackHandler(String storedFileName) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                    // 롤백이 발생했을 때 S3에서 파일 삭제
                    s3Service.deleteFileFromS3(storedFileName);
                }
            }
        });
    }

    // S3에 파일 업로드
    private String uploadToS3(MultipartFile file) {
        try {
            return s3Service.uploadFile(file);
        } catch (IOException e) {
            log.error("파일 업로드 작업 중 예외가 발생하였습니다. 파일명: {}, 크기: {}", file.getOriginalFilename(), file.getSize(), e);
            throw new FileProcessingException("파일 업로드 중 문제가 발생했습니다. 다시 시도해 주세요.");
        }
    }
    // ===================================================== 업로드 ======================================================

    // =============================================== 파일 다운로드와 삭제 =================================================
//    // 파일들 확인 후 업로드하는 메소드
//    public String checkAndUploadFiles(MultipartFile[] files) {
//        // 업로드 파일 개수 확인
//        checkFilesLength(files);
//
//        // 각각의 파일 확인
//        StringBuilder sb = new StringBuilder();
//        for(int i = 0; i < files.length; i++) {
//            validateFile(files[i]);
//            try {
//                sb.append(s3Service.uploadFile(files[i]));
//                if (i != files.length - 1) { sb.append("/"); }
//            } catch (IOException e) {
//                log.error("다중 파일 업로드 작업 중 예외가 발생하였습니다.", e);
//                throw new FileProcessingException("파일 업로드 중 문제가 발생했습니다. 다시 시도해 주세요.");
//            }
//        }
//        return sb.toString();
//    }

    // 파일 다운로드
    @Transactional
    @Override
    public byte[] downloadFile(int fileId) {
        // 파일 정보 조회
        File file = findFileById(fileId);
        log.info("● FileServiceImpl downloadFile 파일받아옴 파일이름 {} ●", file.getOriginalFileName());

        // S3에서 파일 다운로드
        return downloadFileFromS3(file);
    }

    private byte[] downloadFileFromS3(File file) {
        String storedFileName = file.getStoredFileName();
        log.info("● downloadFileFromS3 접근완료 {} ●", file.getStoredFileName());
        return s3Service.downloadFile(storedFileName);
    }

    // 파일 삭제
    @Transactional
    @Override
    public void deleteFile(int fileId) {
        log.debug("fileServiceImpl.deleteFile로 오는 fileId : {}", fileId);
        // 파일 정보 조회
        File file = findFileById(fileId);

        // DB에서 해당 파일 삭제
        fileDAO.deleteFile(fileId);
        log.info("fileDAO.deleteFile 완료");

        // S3에서 제거
        deleteFileFromS3(file);
    }

    @Override // 파일 idList로 파일 삭제
    public void deleteFileByFileIdList(List<Integer> fileIdList) {
        fileDAO.deleteFileByFileIdList(fileIdList);
    }

    private void deleteFileFromS3(File file) {
        String storedFileName = file.getStoredFileName();
        s3Service.deleteFileFromS3(storedFileName);
    }
    // =============================================== 파일 다운로드와 삭제 =================================================

    // ==================================================== 유효성 검사 ===================================================
    // 파일 유효성 검사
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileBadRequestException("파일이 비어있습니다.\n파일을 확인해주세요.");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileBadRequestException("파일 크기가 10MB를 초과합니다.\n파일을 확인해주세요.");
        }
    }

    // 업로드 파일 개수 확인
    private void checkFilesLength(MultipartFile[] files) {
        if (files.length > 3) {
            throw new FileBadRequestException("최대 3개의 파일만 업로드할 수 있습니다.");
        }
    }
    // ==================================================== 유효성 검사 ===================================================
}

