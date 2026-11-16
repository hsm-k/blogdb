package com.example.blogdb.dto;

/**
 * 게임 프로젝트 등록/수정 요청용 DTO (웹 폼 입력값 전달)
 */
public class GameProjectRequestDTO {
    private String title;
    private String genre;
    private String summary;
    private String status;
    private Integer progress;
    private String githubUrl;
    private String playUrl;
    private String thumbnailUrl;

    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getSummary() { return summary; }
    public String getStatus() { return status; }
    public Integer getProgress() { return progress; }
    public String getGithubUrl() { return githubUrl; }
    public String getPlayUrl() { return playUrl; }
    public String getThumbnailUrl() { return thumbnailUrl; }

    public void setTitle(String title) { this.title = title; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setSummary(String summary) { this.summary = summary; }
    public void setStatus(String status) { this.status = status; }
    public void setProgress(Integer progress) { this.progress = progress; }
    public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }
    public void setPlayUrl(String playUrl) { this.playUrl = playUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
}
