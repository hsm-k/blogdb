package com.example.blogdb;

import com.example.blogdb.dto.CommentDTO;
import com.example.blogdb.dto.CommentRequestDTO;
import com.example.blogdb.repository.CommentRepository;
import com.example.blogdb.repository.PostRepository;
import com.example.blogdb.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("댓글 정상 작성 및 게시글 연동 검증 (단계 C)")
    void createComment_success() {
        // given
        Long postId = 1L;
        Post post = new Post();
        post.setTitle("테스트 게시글");

        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        Comment savedComment = new Comment(post, "홍길동", "정말 유익한 글입니다!", LocalDateTime.now());
        given(commentRepository.save(any(Comment.class))).willReturn(savedComment);

        CommentRequestDTO request = new CommentRequestDTO("홍길동", "정말 유익한 글입니다!");

        // when
        CommentDTO result = commentService.createComment(postId, request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAuthor()).isEqualTo("홍길동");
        assertThat(result.getContent()).isEqualTo("정말 유익한 글입니다!");
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    @DisplayName("공백 댓글 작성 시 IllegalArgumentException 예외 발생 (예외 상황 검증)")
    void createComment_blank_throwsException() {
        Long postId = 1L;
        CommentRequestDTO emptyRequest = new CommentRequestDTO("작성자", "   ");

        assertThatThrownBy(() -> commentService.createComment(postId, emptyRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("댓글 내용");
    }

    @Test
    @DisplayName("존재하지 않는 게시글에 댓글 작성 시 예외 발생 (예외 상황 검증)")
    void createComment_notFoundPost_throwsException() {
        Long postId = 999L;
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        CommentRequestDTO request = new CommentRequestDTO("작성자", "댓글 내용");

        assertThatThrownBy(() -> commentService.createComment(postId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("게시글을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("게시글별 댓글 조회 및 격리 검증 (단계 C)")
    void getComments_orderByIdAsc() {
        Long postId = 1L;
        Post post = new Post();
        Comment comment1 = new Comment(post, "익명1", "첫 번째 댓글", LocalDateTime.now());
        Comment comment2 = new Comment(post, "익명2", "두 번째 댓글", LocalDateTime.now());

        given(commentRepository.findByPostIdOrderByIdAsc(postId)).willReturn(List.of(comment1, comment2));

        List<CommentDTO> comments = commentService.getComments(postId);

        assertThat(comments).hasSize(2);
        assertThat(comments.get(0).getContent()).isEqualTo("첫 번째 댓글");
        assertThat(comments.get(1).getContent()).isEqualTo("두 번째 댓글");
        verify(commentRepository).findByPostIdOrderByIdAsc(postId);
    }
}
