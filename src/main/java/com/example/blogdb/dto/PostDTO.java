package com.example.blogdb.dto;

import java.time.LocalDateTime;

public class PostDTO {
    private final Long id;
    private final String title;
    private final String category;
    private final String contentType;
    private final String content;
    private final byte[] imageData;
    private final String imageContentType;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public PostDTO(Long id, String title, String category, String contentType, String content, byte[] imageData, String imageContentType, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.contentType = contentType;
        this.content = content;
        this.imageData = imageData;
        this.imageContentType = imageContentType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getContentType() { return contentType; }
    public String getContent() { return content; }
    public byte[] getImageData() { return imageData; }
    public String getImageContentType() { return imageContentType; }
    public boolean isImageDataPresent() { return imageData != null && imageData.length > 0; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
