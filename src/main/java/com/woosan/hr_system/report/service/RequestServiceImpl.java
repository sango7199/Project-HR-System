package com.woosan.hr_system.report.service;

import com.woosan.hr_system.auth.service.AuthService;
import com.woosan.hr_system.report.dao.RequestDAO;
import com.woosan.hr_system.report.model.Report;
import com.woosan.hr_system.report.model.Request;
import com.woosan.hr_system.search.PageRequest;
import com.woosan.hr_system.search.PageResult;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestDAO requestDAO;
    @Autowired
    private AuthService authService;

    //===================================================CRUD 메소드=======================================================
    @Override // 요청 생성
    public void createRequest(Request request) {
        LocalDateTime requestDate = LocalDateTime.now();

        for (int i = 0; i < request.getNameList().size(); i++) {
            Map<String, Object> params = new HashMap<>();
            params.put("requesterId", request.getRequesterId());
            params.put("writerId", request.getIdList().get(i));
            params.put("writerName", request.getNameList().get(i));
            params.put("dueDate", request.getDueDate());
            params.put("requestNote", request.getRequestNote());
            params.put("requestDate", requestDate);

            requestDAO.createRequest(params);
        }
    }

    @Override  // 로그인한 계정 기준 요청 리스트 조회(내가 쓴 요청 리스트 조회)
    public List<Request> getMyRequests(String employeeId, String requestStart, String requestEnd) {
        // 입력된 날짜를 파싱하기 위한 DateTimeFormatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth startYearMonth;
        YearMonth endYearMonth;

        // 현재 연월 가져오기
        YearMonth currentYearMonth = YearMonth.now();

        // startDate와 endDate가 null인지 확인하고 현재 연월로 설정
        if (requestStart == null) {
            startYearMonth = currentYearMonth;
        } else {
            startYearMonth = YearMonth.parse(requestStart, formatter);
        }

        if (requestEnd == null) {
            endYearMonth = currentYearMonth;
        } else {
            endYearMonth = YearMonth.parse(requestEnd, formatter);
        }
        return requestDAO.getMyRequests(employeeId, startYearMonth, endYearMonth);
    }

    @Override  // 로그인한 계정 기준 내게 온 요청 리스트 조회(내게 온 요청 목록 조회)
    public List<Request> getMyPendingRequests(String writerId) {
        return requestDAO.getMyPendingRequests(writerId);
    }

    @Override // 요청 세부 조회
    public Request getRequestById(Long requestId) {
        return requestDAO.getRequestById(requestId);
    }

    @Override // 요청 수정
    public void updateRequest(Request request) {
        //request 객체 설정
        LocalDateTime modifiedDate = LocalDateTime.now(); //현재 기준 수정 시간 설정

        Map<String, Object> params = new HashMap<>();
        params.put("requestId", request.getRequestId());
        params.put("writerId", request.getIdList().get(0));
        params.put("writerName", request.getNameList().get(0));
        params.put("dueDate", request.getDueDate());
        params.put("requestNote", request.getRequestNote());
        params.put("modifiedDate", modifiedDate);

        requestDAO.updateRequest(params);
    }


    @Override // 요청 삭제
    public void deleteRequest(Long requestId) {
        // shared_trash 테이블에 삭제될 데이터들 삽입
        requestDAO.insertRequestIntoSharedTrash(requestId);
        requestDAO.deleteRequest(requestId);
    }

//===================================================CRUD 메소드=======================================================
//===================================================그 외 메소드=======================================================
    @Override // 요청 검색
    public PageResult<Request> searchRequests(PageRequest pageRequest, String writerId, int searchType, String requestStart, String requestEnd) {
        int offset = pageRequest.getPage() * pageRequest.getSize();
        List<Request> requests = requestDAO.search(pageRequest.getKeyword(), pageRequest.getSize(), offset, writerId, searchType, requestStart, requestEnd);
        int total = requestDAO.count(pageRequest.getKeyword(), writerId, searchType, requestStart, requestEnd);

        return new PageResult<>(requests, (int) Math.ceil((double) total / pageRequest.getSize()), total, pageRequest.getPage());
    }


    @Override // 보고서 결재 처리
    public void updateApprovalStatus(Long reportId, String status, String rejectionReason) {
        // report 객체 설정
        Report report = new Report();
        report.setReportId(reportId);
        report.setStatus(status);
        report.setRejectReason(rejectionReason);

        requestDAO.updateApprovalStatus(report);
    }
//===================================================그 외 메소드=======================================================

}
