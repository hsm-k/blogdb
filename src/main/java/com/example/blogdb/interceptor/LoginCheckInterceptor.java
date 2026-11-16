package com.example.blogdb.interceptor;

import com.example.blogdb.constant.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    // Controller가 실행되기 전에 호출됩니다.
    // true면 Controller로 진행하고, false면 여기서 요청 처리를 멈춥니다.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // false: 세션이 없을 때 빈 세션을 새로 만들지 않고 null을 받습니다.
        HttpSession session = request.getSession(false);

        // 세션 자체가 없거나, 로그인 때 저장한 관리자 정보가 없으면 로그인하지 않은 상태입니다.
        if (session == null || session.getAttribute(SessionConst.LOGIN_ADMIN) == null) {
            // 브라우저를 로그인 화면으로 이동시키고 Controller 실행을 막습니다.
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return false;
        }

        // 유효한 로그인 세션이므로 원래 요청한 Controller를 실행합니다.
        return true;
    }
}
