package com.example.blogdb.dto;

import java.time.LocalDateTime;

/**
 * 게임 프로젝트 조회용 DTO (Entity의 데이터를 안전하게 View로 전달)
 */
public class GameProjectDTO {
    private final Long id;
    private final String title;
    private final String genre;
    private final String summary;
    private final String status;
    private final Integer progress;
    private final String githubUrl;
    private final String playUrl;
    private final String thumbnailUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public GameProjectDTO(Long id, String title, String genre, String summary, String status,
                          Integer progress, String githubUrl, String playUrl, String thumbnailUrl,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.summary = summary;
        this.status = status;
        this.progress = progress != null ? progress : 0;
        this.githubUrl = githubUrl;
        this.playUrl = playUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getSummary() { return summary; }
    public String getStatus() { return status; }
    public Integer getProgress() { return progress; }
    public String getGithubUrl() { return githubUrl; }
    public String getPlayUrl() { return playUrl; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public boolean hasPlayUrl() {
        return playUrl != null && !playUrl.trim().isEmpty();
    }

    public boolean hasGithubUrl() {
        return githubUrl != null && !githubUrl.trim().isEmpty();
    }
}
