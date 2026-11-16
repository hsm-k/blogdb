package com.example.blogdb;

import com.example.blogdb.constant.SessionConst;
import com.example.blogdb.controller.GameProjectController;
import com.example.blogdb.dto.AdminSessionDTO;
import com.example.blogdb.dto.GameProjectDTO;
import com.example.blogdb.service.GameProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class GameProjectControllerTest {

    private MockMvc mockMvc;
    private MockHttpSession adminSession;

    @Mock
    private GameProjectService gameProjectService;

    @InjectMocks
    private GameProjectController gameProjectController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(gameProjectController).build();
        adminSession = new MockHttpSession();
        adminSession.setAttribute(SessionConst.LOGIN_ADMIN, new AdminSessionDTO(1L, "admin", "관리자"));
    }

    @Test
    @DisplayName("[게임 프로젝트] 일반 사용자용 프로젝트 목록 조회")
    void publicProjectList_success() throws Exception {
        GameProjectDTO project = new GameProjectDTO(
                1L, "마블 벤치", "3D 퍼즐", "설명", "개발 중", 70,
                "https://github.com/example/repo", "https://play.unity.com", null,
                LocalDateTime.now(), LocalDateTime.now()
        );
        given(gameProjectService.getProjects()).willReturn(List.of(project));

        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(view().name("project-list"))
                .andExpect(model().attributeExists("projects"));
    }

    @Test
    @DisplayName("[게임 프로젝트] 일반 사용자용 프로젝트 상세 조회")
    void publicProjectDetail_success() throws Exception {
        Long projectId = 1L;
        GameProjectDTO project = new GameProjectDTO(
                projectId, "마블 벤치", "3D 퍼즐", "설명", "개발 중", 70,
                "https://github.com/example/repo", "https://play.unity.com", null,
                LocalDateTime.now(), LocalDateTime.now()
        );
        given(gameProjectService.getProject(projectId)).willReturn(project);

        mockMvc.perform(get("/projects/{id}", projectId))
                .andExpect(status().isOk())
                .andExpect(view().name("project-detail"))
                .andExpect(model().attribute("project", project));
    }

    @Test
    @DisplayName("[게임 프로젝트] 관리자 프로젝트 등록 요청 성공")
    void adminProjectCreate_success() throws Exception {
        mockMvc.perform(post("/admin/projects/create")
                        .session(adminSession)
                        .param("title", "신규 프로젝트")
                        .param("genre", "2D 플랫포머")
                        .param("status", "기획 중")
                        .param("progress", "10")
                        .param("summary", "기획 내용"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/projects"));
    }

    @Test
    @DisplayName("[게임 프로젝트] 관리자 프로젝트 수정 폼 조회 성공")
    void adminProjectEditForm_success() throws Exception {
        Long projectId = 1L;
        GameProjectDTO project = new GameProjectDTO(
                projectId, "마블 벤치", "3D 퍼즐", "설명", "개발 중", 70,
                "https://github.com/example/repo", "https://play.unity.com", null,
                LocalDateTime.now(), LocalDateTime.now()
        );
        given(gameProjectService.getProject(projectId)).willReturn(project);

        mockMvc.perform(get("/admin/projects/{id}/edit", projectId)
                        .session(adminSession))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-project-edit"))
                .andExpect(model().attribute("project", project));
    }

    @Test
    @DisplayName("[게임 프로젝트] 관리자 프로젝트 수정 요청 성공 (진행도 및 정보 갱신)")
    void adminProjectEdit_success() throws Exception {
        Long projectId = 1L;

        mockMvc.perform(post("/admin/projects/{id}/edit", projectId)
                        .session(adminSession)
                        .param("title", "수정된 프로젝트")
                        .param("genre", "3D 액션")
                        .param("status", "개발 중")
                        .param("progress", "85")
                        .param("summary", "진행도 85% 달성"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/projects"));
    }

    @Test
    @DisplayName("[게임 프로젝트] 비로그인 상태에서 수정 폼 접근 시 로그인 페이지로 리다이렉트")
    void adminProjectEditForm_unauthorized() throws Exception {
        mockMvc.perform(get("/admin/projects/1/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login"));
    }

    @Test
    @DisplayName("[게임 프로젝트] 비로그인 상태에서 수정 요청 시 로그인 페이지로 리다이렉트 (수정 불가 차단)")
    void adminProjectEdit_unauthorized() throws Exception {
        mockMvc.perform(post("/admin/projects/1/edit")
                        .param("title", "해킹 시도")
                        .param("progress", "100"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login"));
    }

    @Test
    @DisplayName("[게임 프로젝트] 관리자 프로젝트 삭제 요청 성공")
    void adminProjectDelete_success() throws Exception {
        Long projectId = 1L;

        mockMvc.perform(post("/admin/projects/{id}/delete", projectId)
                        .session(adminSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/projects"));
    }
}
