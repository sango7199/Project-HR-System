package com.woosan.hr_system.report.service;

import com.woosan.hr_system.report.dao.ReportRequestDAO;
import com.woosan.hr_system.report.model.Report;
import com.woosan.hr_system.report.model.ReportRequest;
import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportRequestServiceImpl implements ReportRequestService {

    @Autowired
    private ReportRequestDAO reportRequestDAO;

    @Override // 요청 생성
    public void createRequest(String employeeId, LocalDate dueDateSql, String requestNote, LocalDateTime requestDate) {
        ReportRequest request = new ReportRequest();
        LocalDateTime now = LocalDateTime.now();

        request.setRequestDate(now);
        request.setEmployeeId(employeeId);
        request.setDueDate(dueDateSql);
        request.setRequestNote(requestNote);
        reportRequestDAO.insertRequest(request);
    }

    @Override // 모든 요청 조회
    public List<ReportRequest> getAllReportRequests() {
        return reportRequestDAO.getAllReportRequests();
    }

    @Override // 특정 요청 조회
    public ReportRequest getRequestById(Long requestId) {
        return reportRequestDAO.getRequestById(requestId);
    }

    @Override
    public void updateRequest(Long requestId, String employeeId,String requestNote,LocalDate dueDate) {
        System.out.println("서비스");
        ReportRequest request = reportRequestDAO.getRequestById(requestId);
        request.setEmployeeId(employeeId);
        request.setRequestNote(requestNote);
        request.setDueDate(dueDate);
        reportRequestDAO.updateRequest(request);
    }

    @Override
    public void deleteRequest(Long requestId) {
        reportRequestDAO.deleteRequest(requestId);
    }
















//    @Override // 작성 요청 생성
//    public void createReportRequest(ReportRequest request) {
//        reportRequestDAO.createReportRequest(request);
//    }
//
//    @Override // 보고서 기반 작성 요청 조회
//    public ReportRequest getReportRequestById(int requestId) {
//        return reportRequestDAO.getReportRequestById(requestId);
//    }
//
//    @Override // 작성 요청 수정
//    public void updateReportRequest(ReportRequest request) {
//        reportRequestDAO.updateReportRequest(request);
//    }
//
//    @Override // 특정 작성 요청 삭제
//    public void deleteReportRequest(int requestId) {
//        reportRequestDAO.deleteReportRequest(requestId);
//    }
//
//    @Override // 사원 기반 보고서 조회
//    public List<ReportRequest> getReportRequestsByEmployeeId(String employeeId) {
//        return reportRequestDAO.getReportRequestsByEmployeeId(employeeId);
//    }
}
