package com.example.blogdb.service;

import com.example.blogdb.Comment;
import com.example.blogdb.Post;
import com.example.blogdb.dto.CommentDTO;
import com.example.blogdb.dto.CommentRequestDTO;
import com.example.blogdb.repository.CommentRepository;
import com.example.blogdb.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    // Spring이 연결한 Repository 구현을 생성자로 주입받음 (PPT 20p)
    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    // 게시글 ID에 해당하는 댓글 목록 조회 (파생 쿼리 findByPostIdOrderByIdAsc 사용 - PPT 22p, 30~34p)
    @Transactional(readOnly = true)
    public List<CommentDTO> getComments(Long postId) {
        return commentRepository.findByPostIdOrderByIdAsc(postId)
                .stream()
                .map(this::toCommentDTO)
                .toList();
    }

    // 댓글 생성 및 저장 (PPT 2p, 23~24p)
    public CommentDTO createComment(Long postId, CommentRequestDTO request) {
        // 공백 댓글 예외 처리 (PPT 37p)
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용을 입력해주세요.");
        }

        // 대상 게시글 조회 (findById와 Optional 분기 처리 - PPT 23p, 25p)
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + postId));

        String author = (request.getAuthor() == null || request.getAuthor().trim().isEmpty())
                ? "방문자"
                : request.getAuthor().trim();

        // 새 엔티티 객체 생성
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setContent(request.getContent().trim());
        comment.setCreatedAt(LocalDateTime.now());

        // repository.save()를 통한 저장 요청 및 반환된 Entity 사용
        Comment savedComment = commentRepository.save(comment);

        return toCommentDTO(savedComment);
    }

    private CommentDTO toCommentDTO(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getPost().getId(),
                comment.getAuthor(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}
