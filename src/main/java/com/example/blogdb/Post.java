package com.example.blogdb;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String category;
    private String contentType;
    private String content;
    @Lob
    private byte[] imageData;
    private String imageContentType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 게시글 1개 : 콘텐츠 모듈 N개
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC, id ASC")
    private List<ContentModule> contentModules = new ArrayList<>();

    // 게시글 1개 : 댓글 N개
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<Comment> comments = new ArrayList<>();

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getContentType() { return contentType; }
    public String getContent() { return content; }
    public byte[] getImageData() { return imageData; }
    public String getImageContentType() { return imageContentType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<ContentModule> getContentModules() { return contentModules; }
    public List<Comment> getComments() { return comments; }
    public void setTitle(String title) { this.title = title; }
    public void setCategory(String category) { this.category = category; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public void setContent(String content) { this.content = content; }
    public void setImageData(byte[] imageData) { this.imageData = imageData; }
    public void setImageContentType(String imageContentType) { this.imageContentType = imageContentType; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }

    public void addContentModule(ContentModule module) {
        contentModules.add(module);
        module.setPost(this);
    }

    public void removeContentModule(ContentModule module) {
        contentModules.remove(module);
        module.setPost(null);
    }
}
