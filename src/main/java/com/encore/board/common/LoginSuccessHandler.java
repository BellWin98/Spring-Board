package com.encore.board.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// 로그인이 성공했을 때 실행할 로직 정의
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 사용자 고유의 JSESSION ID가 request안에 들어있음.
        HttpSession httpSession = request.getSession();

        // 로그인이 성공하면 Authentication 객체 안에 User 객체가 들어가게 되고, getName은 email을 의미
        // 홈 화면에 로그인 한 사용자 이메일 출력
        httpSession.setAttribute("email", authentication.getName());
        response.sendRedirect("/");
    }
}
