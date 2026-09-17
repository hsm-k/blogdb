package com.example.blogdb.repository;

import com.example.blogdb.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    Optional<Blog> findTopByOrderByIdDesc();
}