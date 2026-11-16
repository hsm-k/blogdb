package com.example.blogdb.controller;

import com.example.blogdb.constant.SessionConst;
import com.example.blogdb.dto.AdminLoginRequestDTO;
import com.example.blogdb.dto.AdminSessionDTO;
import com.example.blogdb.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class AdminController {

    // @RequiredArgsConstructor가 AdminService를 주입받는 생성자를 자동으로 만듭니다.
    private final AdminService adminService;

    // GET /admin/login: 로그인 입력 화면을 보여 줍니다.
    @GetMapping("/admin/login")
    public String loginForm() {
        return "admin-login";
    }

    @PostMapping("/admin/login")
    public String login(@ModelAttribute AdminLoginRequestDTO loginRequest,
                        HttpServletRequest request,
                        Model model) {
        // Service에 아이디와 비밀번호 검증을 요청합니다.
        AdminSessionDTO loginAdmin = adminService.login(loginRequest).orElse(null);

        // 로그인 실패는 세션을 만들지 않고, 오류 문구와 함께 로그인 화면을 다시 보여 줍니다.
        if (loginAdmin == null) {
            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 틀렸습니다.");
            return "admin-login";
        }

        // 기존 세션이 있다면 먼저 제거합니다. 이후 새 세션을 발급하기 위함입니다.
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        // true: 세션이 없으면 새 세션을 생성합니다.
        HttpSession newSession = request.getSession(true);
        // "loginAdmin"이라는 이름으로 로그인 관리자 정보를 서버 세션에 저장합니다.
        newSession.setAttribute(SessionConst.LOGIN_ADMIN, loginAdmin);
        return "redirect:/admin";
    }

    // 세션 검증은 LoginCheckInterceptor가 담당합니다.
    // 따라서 이 메서드에는 session == null 같은 중복 검사 코드가 필요 없습니다.
    @GetMapping("/admin")
    public String adminHome(@SessionAttribute(SessionConst.LOGIN_ADMIN) AdminSessionDTO admin,
                            Model model) {
        // 세션에서 꺼낸 관리자 정보를 Thymeleaf HTML에 전달합니다.
        model.addAttribute("admin", admin);
        return "admin";
    }

    @PostMapping("/admin/logout")
    public String logout(HttpSession session) {
        // 현재 브라우저의 세션과 그 안의 loginAdmin 정보를 모두 무효화합니다.
        session.invalidate();
        return "redirect:/admin/login";
    }
}
