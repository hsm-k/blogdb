package com.example.blogdb.controller;

import com.example.blogdb.constant.SessionConst;
import com.example.blogdb.dto.GameProjectDTO;
import com.example.blogdb.dto.GameProjectRequestDTO;
import com.example.blogdb.service.GameProjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * 게임 프로젝트 웹 요청 처리 Controller (MVC 구조)
 */
@Controller
public class GameProjectController {

    private final GameProjectService gameProjectService;

    public GameProjectController(GameProjectService gameProjectService) {
        this.gameProjectService = gameProjectService;
    }

    // 일반 방문자용 게임 프로젝트 목록 화면
    @GetMapping("/projects")
    public String publicProjectList(Model model) {
        List<GameProjectDTO> projects = gameProjectService.getProjects();
        model.addAttribute("projects", projects);
        return "project-list";
    }

    // 일반 방문자용 게임 프로젝트 상세 화면 (개요, 진행도, GitHub, 플레이어)
    @GetMapping("/projects/{id}")
    public String publicProjectDetail(@PathVariable Long id, Model model) {
        try {
            GameProjectDTO project = gameProjectService.getProject(id);
            model.addAttribute("project", project);
            return "project-detail";
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다.", e);
        }
    }
    // Unity 게임 플레이 화면
    @GetMapping("/game")
    public String game() {
        return "game";
    }

    private boolean isNotAdmin(HttpSession session) {
        return session == null || session.getAttribute(SessionConst.LOGIN_ADMIN) == null;
    }

    // 관리자: 프로젝트 관리 목록
    @GetMapping("/admin/projects")
    public String adminProjectList(HttpSession session, Model model) {
        if (isNotAdmin(session)) {
            return "redirect:/admin/login";
        }
        List<GameProjectDTO> projects = gameProjectService.getProjects();
        model.addAttribute("projects", projects);
        return "admin-project-list";
    }

    // 관리자: 새 프로젝트 등록 폼
    @GetMapping("/admin/projects/create")
    public String adminProjectCreateForm(HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/admin/login";
        }
        return "admin-project-form";
    }

    // 관리자: 새 프로젝트 등록 처리
    @PostMapping("/admin/projects/create")
    public String adminProjectCreate(@ModelAttribute GameProjectRequestDTO request, HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/admin/login";
        }
        GameProjectDTO project = gameProjectService.createProject(request);
        return "redirect:/admin/projects";
    }

    // 관리자: 프로젝트 삭제 처리
    @PostMapping("/admin/projects/{id}/delete")
    public String adminProjectDelete(@PathVariable Long id, HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/admin/login";
        }
        gameProjectService.deleteProject(id);
        return "redirect:/admin/projects";
    }

    // 관리자: 프로젝트 수정 폼
    @GetMapping("/admin/projects/{id}/edit")
    public String adminProjectEditForm(@PathVariable Long id, HttpSession session, Model model) {
        if (isNotAdmin(session)) {
            return "redirect:/admin/login";
        }
        try {
            GameProjectDTO project = gameProjectService.getProject(id);
            model.addAttribute("project", project);
            return "admin-project-edit";
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다.", e);
        }
    }

    // 관리자: 프로젝트 수정 처리
    @PostMapping("/admin/projects/{id}/edit")
    public String adminProjectEdit(@PathVariable Long id,
                                   @ModelAttribute GameProjectRequestDTO request,
                                   HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/admin/login";
        }
        gameProjectService.updateProject(id, request);
        return "redirect:/admin/projects";
    }
}
