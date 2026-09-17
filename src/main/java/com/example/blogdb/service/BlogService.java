package com.example.blogdb.service;

import com.example.blogdb.Blog;
import com.example.blogdb.repository.BlogRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import com.example.blogdb.dto.BlogTitleDTO;
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public void saveTitle(String title) {

        Blog blog = new Blog();

        blog.setTitle(title);
        blog.setSavedDate("2026-09-03");

        blogRepository.save(blog);
    }

    public BlogTitleDTO getLatestTitle() {

    Optional<Blog> blog = blogRepository.findTopByOrderByIdDesc();

    if (blog.isPresent()) {
        return new BlogTitleDTO(blog.get().getTitle());
    }

    return new BlogTitleDTO("저장된 글이 없습니다.");
}
}