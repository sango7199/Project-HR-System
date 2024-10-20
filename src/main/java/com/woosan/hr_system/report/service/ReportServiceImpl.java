package com.woosan.hr_system.report.service;

import com.woosan.hr_system.auth.model.UserSessionInfo;
import com.woosan.hr_system.employee.dao.EmployeeDAO;
import com.woosan.hr_system.employee.model.Employee;
import com.woosan.hr_system.report.dao.ReportDAO;
import com.woosan.hr_system.report.dao.ReportFileDAO;
import com.woosan.hr_system.report.model.Report;
import com.woosan.hr_system.report.model.ReportStat;
import com.woosan.hr_system.report.model.Request;
import com.woosan.hr_system.search.PageRequest;
import com.woosan.hr_system.search.PageResult;
import com.woosan.hr_system.file.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportDAO reportDAO;
    @Autowired
    private EmployeeDAO employeeDAO;
    @Autowired
    private FileService fileService;
    @Autowired
    private ReportFileDAO reportFileDAO;
    @Autowired
    private ReportFileService reportFileService;
    @Autowired
    private RequestService requestService;


    //=====================================================생성 메소드======================================================
    @Override // 보고서 생성
    public List<Integer> createReport(Report report) {

        List<Integer> reportIdList = new ArrayList<>();

        Map<String, Object> params = new HashMap<>();
        params.put("writerId", report.getWriterId());
        params.put("title", report.getTitle());
        params.put("content", report.getContent());
        params.put("status", "미처리");
        params.put("createdDate", report.getCreatedDate());
        params.put("completeDate", report.getCompleteDate());

        for (int i = 0; i < report.getNameList().size(); i++) {
            params.put("approverId", report.getIdList().get(i));
            params.put("approverName", report.getNameList().get(i));
            int reportId = reportDAO.createReport(params);
            reportIdList.add(reportId);
        }
        return reportIdList;
    }

    @Override // 보고서 + 파일 생성
    public void createReportWithFile(Report report, List<MultipartFile> reportDocuments) {

        List<Integer> fileIdlist = new ArrayList<>();
        List<Integer> reportIdlist = new ArrayList<>();

        Map<String, Object> params = new HashMap<>();
        params.put("writerId", report.getWriterId());
        params.put("title", report.getTitle());
        params.put("content", report.getContent());
        params.put("createdDate", report.getCreatedDate());
        params.put("status", "미처리");
        params.put("completeDate", report.getCompleteDate());

        for (int i = 0; i < report.getNameList().size(); i++) {
            params.put("approverId", report.getIdList().get(i));
            params.put("approverName", report.getNameList().get(i));
            int reportId = reportDAO.createReport(params); // 생성된 reportId 가져옴
            reportIdlist.add(reportId);
        }

        if (reportDocuments != null) {
            // 파일들 체크 후 DB에 저장할 파일명 반환
            for (MultipartFile reportdocument : reportDocuments) {
                int fileId = fileService.uploadingFile(reportdocument, "report"); // 생성된 fileId 가져옴
                fileIdlist.add(fileId);
            }
        }

        // reportId와 fileId를 모두 순회하며 조인테이블 삽입
        for (int reportId : reportIdlist) {
            for (int fileId : fileIdlist) {
                reportFileDAO.createReportFile(reportId, fileId);
            }
        }
    }

    @Override // 요청 들어온 보고서 작성
    public int createReportFromRequest(Report report) {
        Map<String, Object> params = new HashMap<>();
        params.put("writerId", report.getWriterId());
        params.put("approverId", report.getIdList().get(0));
        params.put("approverName", report.getNameList().get(0));
        params.put("title", report.getTitle());
        params.put("content", report.getContent());
        params.put("createdDate", report.getCreatedDate());
        params.put("status", "미처리");
        params.put("completeDate", report.getCompleteDate());

        int reportId = reportDAO.createReport(params);
        return reportId;
    }

    @Override // 요청 들어온 보고서 + 파일 작성
    public int createReportFromRequestWithFile(Report report, List<MultipartFile> reportDocuments) {
        // 조인 테이블에 연결 할 file, report Id리스트 생성
        List<Integer> fileIdlist = new ArrayList<>();

        Map<String, Object> params = new HashMap<>();
        params.put("writerId", report.getWriterId());
        params.put("approverId", report.getIdList().get(0));
        params.put("approverName", report.getNameList().get(0));
        params.put("title", report.getTitle());
        params.put("content", report.getContent());
        params.put("createdDate", report.getCreatedDate());
        params.put("status", "미처리");
        params.put("completeDate", report.getCompleteDate());

        int reportId = reportDAO.createReport(params);

        if (reportDocuments != null) {
            // 파일들 체크 후 DB에 저장할 파일명 반환
            for (MultipartFile reportdocument : reportDocuments) {
                int fileId = fileService.uploadingFile(reportdocument, "report"); // 생성된 fileId 가져옴
                fileIdlist.add(fileId);
            }
        }

        // reportId와 fileId를 모두 순회하며 조인테이블 삽입
        for (int fileId : fileIdlist) {
            reportFileDAO.createReportFile(reportId, fileId);
        }

        return reportId;
    }

//=====================================================생성 메소드======================================================
//=====================================================조회 메소드======================================================
    @Override // 모든 보고서 조회
    public List<Report> getAllReports(String employeeId) {
        return reportDAO.getAllReports(employeeId);
    }

    @Override // 특정 보고서 조회
    public Report getReportById(int reportId) {
        UserSessionInfo userSessionInfo = new UserSessionInfo();
        String currentEmployeeId = userSessionInfo.getCurrentEmployeeId();
        Report report = checkReportAuthorization(reportId, currentEmployeeId);

        return report;
    }


    @Override // 최근 5개 보고서 조회
    public List<Report> getRecentReports(String writerId) {
        return reportDAO.getRecentReports(writerId);
    }

    @Override // 결재 미처리 보고서 조회(MANAGER)
    public List<Report> getUnprocessedReports(String approverId) {
        return reportDAO.getUnprocessedReports(approverId);
    }




    @Override // 보고서 검색
    public PageResult<Report> searchReports(PageRequest pageRequest, String writerId, Integer searchType, String approvalStatus, LocalDate startDate, LocalDate endDate) {

        // 보여줄 리스트의 범위를 지정
        int offset = pageRequest.getPage() * pageRequest.getSize();
        // 범위에 속하는 보고서를 검색함
        List<Report> reports = reportDAO.search(pageRequest.getKeyword(), pageRequest.getSize(), offset, writerId, searchType, approvalStatus, startDate, endDate);
        // 범위에 속하는 보고서 개수를 세서 페이징
        int total = reportDAO.count(pageRequest.getKeyword(), writerId, searchType, approvalStatus, startDate, endDate);

        return new PageResult<>(reports, (int) Math.ceil((double) total / pageRequest.getSize()), total, pageRequest.getPage());
    }

    // 결재할 보고서 검색
    @Override
    public PageResult<Report> toApproveSearchReports(PageRequest pageRequest, String approverId, Integer searchType, String approvalStatus, LocalDate startDate, LocalDate endDate) {
        // 보여줄 리스트의 범위를 지정
        int offset = pageRequest.getPage() * pageRequest.getSize();
        // 범위에 속하는 보고서를 검색함
        List<Report> reports = reportDAO.toApproveSearch(pageRequest.getKeyword(), pageRequest.getSize(), offset, approverId, searchType, approvalStatus, startDate, endDate);
        // 범위에 속하는 보고서 개수를 세서 페이징
        int total = reportDAO.toApproveCount(pageRequest.getKeyword(), approverId, searchType, approvalStatus, startDate, endDate);

        return new PageResult<>(reports, (int) Math.ceil((double) total / pageRequest.getSize()), total, pageRequest.getPage());
    }


    @Override // 보고서 통계 조회
    public List<ReportStat> getReportStats(LocalDate startDate, LocalDate endDate, List<String> writerIdList) {
        return reportDAO.getReportStats(startDate, endDate, writerIdList);
    }
//=====================================================조회 메소드======================================================
//=====================================================수정 메소드======================================================
    @Transactional
    @Override // 보고서 수정
    public void updateReport(Report report, List<MultipartFile> toUploadFileList, List<Integer> userSelectedFileIdList) {
        UserSessionInfo userSessionInfo = new UserSessionInfo();
        List<Integer> createdReportIdList = new ArrayList<>();
        List<Integer> existingFileIdList = reportFileService.getFileIdsByReportId(report.getReportId());

        // 결재자 수에 따른 처리
        if (report.getIdList().size() > 1) {
            // 결재자 수가 여러명으로 바뀐 경우
            report.setCreatedDate(userSessionInfo.getNow()); // 현재시간 설정
            createdReportIdList = createReport(report); // 보고서 생성 후 reportId 반환
            reportFileService.updateReportFile(report, toUploadFileList, userSelectedFileIdList, existingFileIdList, createdReportIdList);
            deleteReport(report.getReportId());
        } else {
            report.setModifiedDate(userSessionInfo.getNow()); // 현재시간 설정
            report.setApproverId(report.getIdList().get(0));
            report.setApproverName(report.getNameList().get(0));
            reportDAO.updateReport(report);
            createdReportIdList.add(report.getReportId()); // 조인테이블 수정
            reportFileService.updateReportFile(report, toUploadFileList, userSelectedFileIdList, existingFileIdList, createdReportIdList);
        }
    }
    @Override // 보고서 결재 처리
    public void updateApprovalStatus(int reportId, String status, String rejectionReason) {
        // report 객체 설정
        Report report = new Report();
        report.setReportId(reportId);
        report.setStatus(status);
        report.setRejectReason(rejectionReason);

        reportDAO.updateApprovalStatus(report);
    }
//=====================================================수정 메소드======================================================
//=====================================================삭제 메소드======================================================
    @Transactional
    @Override // 보고서 삭제
    public void deleteReport(int reportId) {
        // shared_trash 테이블에 삭제될 데이터들 삽입
        reportDAO.insertReportIntoSharedTrash(reportId);

        // 보고서 삭제
        reportDAO.deleteReport(reportId);

        // 요청 된 보고서라면 requset테이블의 reportId 삭제
        if (requestService.getRequestByReportId(reportId) > 0){
            requestService.deleteReportId(reportId);
        }

        // 조인테이블 서비스로 reportId를 보내줌 -> 파일 삭제
        reportFileService.deleteReportFileByReportId(reportId);
    }
//=====================================================삭제 메소드======================================================
//=====================================================기타 메소드======================================================
    // 요청에 대한 접근 권한이 있는지 확인
    public Report checkReportAuthorization(int reportId, String currentEmployeeId) {
        Report report = reportDAO.getReportById(reportId); // 요청 세부 정보 가져오기
        if (report == null) {
            throw new IllegalArgumentException("해당 요청이 존재하지 않습니다.");
        }
        // 결재자와 로그인한 사용자가 동일하지 않으면 권한 오류 발생
        if (!report.getApproverId().equals(currentEmployeeId)) {
            if (!report.getWriterId().equals(currentEmployeeId)) {
                throw new SecurityException("권한이 없습니다.");
            }
        }
        return report; // 요청 정보 반환
    }
//=====================================================기타 메소드======================================================

}



