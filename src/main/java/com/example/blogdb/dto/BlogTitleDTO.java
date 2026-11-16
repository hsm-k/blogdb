package com.example.blogdb.dto;

public class BlogTitleDTO {

    private final String title;
    private final String subtitle;

    public BlogTitleDTO(String title) {
        this(title, "배운 것을 기록하고, 만든 것을 나누는 개발 블로그입니다.");
    }

    public BlogTitleDTO(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
