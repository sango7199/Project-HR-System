package com.woosan.hr_system.auth.service;

import com.woosan.hr_system.auth.dao.PasswordDAOImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private PasswordDAOImpl passwordDAO;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String employeeId = request.getParameter("username");

        // 로그인 성공 시 비밀번호 카운트 초기화
        passwordDAO.resetPasswordCount(employeeId);

        log.info("'{}' 사원이 로그인하였습니다.", employeeId);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
