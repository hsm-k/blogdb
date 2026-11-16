package com.example.blogdb.dto;

import java.time.LocalDateTime;

public class CommentDTO {
    private final Long id;
    private final Long postId;
    private final String author;
    private final String content;
    private final LocalDateTime createdAt;

    public CommentDTO(Long id, Long postId, String author, String content, LocalDateTime createdAt) {
        this.id = id;
        this.postId = postId;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
