package com.example.blogdb;

import com.example.blogdb.controller.PostController;
import com.example.blogdb.dto.CommentDTO;
import com.example.blogdb.dto.ContentModuleDTO;
import com.example.blogdb.dto.PostDTO;
import com.example.blogdb.service.CommentService;
import com.example.blogdb.service.ContentModuleService;
import com.example.blogdb.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PostService postService;

    @Mock
    private ContentModuleService contentModuleService;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private PostController postController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Test
    @DisplayName("[단계 A] 게시글 목록 조회 화면 요청 성공")
    void publicPostList_success() throws Exception {
        PostDTO post = new PostDTO(1L, "테스트 제목", "카테고리", "TEXT", "내용", null, null, LocalDateTime.now(), LocalDateTime.now());
        given(postService.getPosts()).willReturn(List.of(post));

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("post-list"))
                .andExpect(model().attributeExists("posts"));
    }

    @Test
    @DisplayName("[단계 B & C] 게시글 상세 화면 조회 시 본문 블록 및 댓글 목록 전달")
    void publicPostDetail_success() throws Exception {
        Long postId = 1L;
        PostDTO post = new PostDTO(postId, "테스트 제목", "카테고리", "TEXT", "내용", null, null, LocalDateTime.now(), LocalDateTime.now());
        ContentModuleDTO module = new ContentModuleDTO(10L, ContentModuleType.TEXT, "상세 텍스트 블록", null, 1);
        CommentDTO comment = new CommentDTO(100L, postId, "익명", "댓글 내용", LocalDateTime.now());

        given(postService.getPost(postId)).willReturn(post);
        given(contentModuleService.getContentModules(postId)).willReturn(List.of(module));
        given(commentService.getComments(postId)).willReturn(List.of(comment));

        mockMvc.perform(get("/posts/{id}", postId))
                .andExpect(status().isOk())
                .andExpect(view().name("post-detail"))
                .andExpect(model().attribute("post", post))
                .andExpect(model().attributeExists("contentModules"))
                .andExpect(model().attributeExists("comments"));
    }

    @Test
    @DisplayName("[예외 상황] 없는 게시글 조회 시 404 상태코드 응답")
    void publicPostDetail_notFound_returns404() throws Exception {
        Long invalidId = 9999L;
        given(postService.getPost(invalidId)).willThrow(new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        mockMvc.perform(get("/posts/{id}", invalidId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("[단계 C] 댓글 작성 성공 시 상세 페이지로 리다이렉트")
    void addComment_success_redirect() throws Exception {
        Long postId = 1L;
        CommentDTO created = new CommentDTO(100L, postId, "작성자", "댓글 내용", LocalDateTime.now());
        given(commentService.createComment(eq(postId), any())).willReturn(created);

        mockMvc.perform(post("/posts/{id}/comments", postId)
                        .param("author", "홍길동")
                        .param("content", "잘 읽었습니다."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));
    }

    @Test
    @DisplayName("[예외 상황] 공백 댓글 작성 시 에러 플래시 속성과 함께 리다이렉트")
    void addComment_blankContent_flashError() throws Exception {
        Long postId = 1L;

        mockMvc.perform(post("/posts/{id}/comments", postId)
                        .param("author", "홍길동")
                        .param("content", "    "))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"))
                .andExpect(flash().attributeExists("errorMessage"));
    }
}
