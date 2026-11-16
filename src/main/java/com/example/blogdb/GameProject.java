package com.example.blogdb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * 게임 프로젝트 Entity (PLAYLOG 핵심 모델)
 * 게임개발 블로그에서 개별 게임 프로젝트의 정보(이름, 장르, 개요, 진행률, GitHub/플레이 링크)를 관리합니다.
 */
@Entity
@Table(name = "game_project")
public class GameProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String genre;

    @Lob
    private String summary;

    // 개발 상태: "기획 중", "개발 중", "테스트 중", "출시 완료" 등
    private String status;

    // 개발 진행률 (0 ~ 100)
    private Integer progress;

    // GitHub 공개 저장소 주소
    private String githubUrl;

    // Unity WebGL 게임 실행 주소 또는 외부 플레이 링크
    private String playUrl;

    // 프로젝트 대표 썸네일 이미지 주소
    private String thumbnailUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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

    public void setTitle(String title) { this.title = title; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setSummary(String summary) { this.summary = summary; }
    public void setStatus(String status) { this.status = status; }
    public void setProgress(Integer progress) { this.progress = progress; }
    public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }
    public void setPlayUrl(String playUrl) { this.playUrl = playUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
