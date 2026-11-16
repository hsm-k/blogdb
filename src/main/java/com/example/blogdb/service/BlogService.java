package com.example.blogdb.service;

import com.example.blogdb.Blog;
import com.example.blogdb.repository.BlogRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import com.example.blogdb.dto.BlogTitleDTO;
@Service
public class BlogService {

    private static final String DEFAULT_SUBTITLE = "배운 것을 기록하고, 만든 것을 나누는 개발 블로그입니다.";

    private final BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public void saveTitle(String title) {

        Blog blog = new Blog();

        blog.setTitle(title);
        blog.setSubtitle(DEFAULT_SUBTITLE);
        blog.setSavedDate("2026-09-03");

        blogRepository.save(blog);
    }

    public BlogTitleDTO getLatestTitle() {

    Optional<Blog> blog = blogRepository.findTopByOrderByIdDesc();

    if (blog.isPresent()) {
        String subtitle = blog.get().getSubtitle();
        return new BlogTitleDTO(blog.get().getTitle(),
                subtitle == null || subtitle.isBlank() ? DEFAULT_SUBTITLE : subtitle);
    }

    return new BlogTitleDTO("저장된 글이 없습니다.");
}
}
