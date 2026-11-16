package com.example.blogdb.repository;

import com.example.blogdb.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 게시글의 댓글을 ID 오름차순(작성 순서)으로 조회하는 파생 쿼리 메서드
    List<Comment> findByPostIdOrderByIdAsc(Long postId);
}
