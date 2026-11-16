package com.example.blogdb.repository;

import com.example.blogdb.ContentModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentModuleRepository extends JpaRepository<ContentModule, Long> {
    List<ContentModule> findAllByPostIdOrderByDisplayOrderAscIdAsc(Long postId);
}
