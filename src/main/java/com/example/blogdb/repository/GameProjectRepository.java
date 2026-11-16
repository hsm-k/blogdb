package com.example.blogdb.repository;

import com.example.blogdb.GameProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 게임 프로젝트 데이터베이스 접근 인터페이스 (Spring Data JPA)
 */
public interface GameProjectRepository extends JpaRepository<GameProject, Long> {
    List<GameProject> findAllByOrderByCreatedAtDesc();
}
