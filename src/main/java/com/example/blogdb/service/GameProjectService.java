package com.example.blogdb.service;

import com.example.blogdb.GameProject;
import com.example.blogdb.dto.GameProjectDTO;
import com.example.blogdb.dto.GameProjectRequestDTO;
import com.example.blogdb.repository.GameProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 게임 프로젝트 비즈니스 로직 처리 Service
 */
@Service
@Transactional
public class GameProjectService {

    private final GameProjectRepository gameProjectRepository;

    public GameProjectService(GameProjectRepository gameProjectRepository) {
        this.gameProjectRepository = gameProjectRepository;
    }

    // 등록된 전체 게임 프로젝트 목록 조회 (최신 등록순)
    @Transactional(readOnly = true)
    public List<GameProjectDTO> getProjects() {
        return gameProjectRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // 게임 프로젝트 상세 단건 조회
    @Transactional(readOnly = true)
    public GameProjectDTO getProject(Long id) {
        GameProject project = findEntity(id);
        return toDTO(project);
    }

    // 신규 게임 프로젝트 생성 (관리자)
    public GameProjectDTO createProject(GameProjectRequestDTO request) {
        GameProject project = new GameProject();
        project.setTitle(request.getTitle());
        project.setGenre(request.getGenre());
        project.setSummary(request.getSummary());
        project.setStatus(request.getStatus() != null ? request.getStatus() : "개발 중");
        project.setProgress(request.getProgress() != null ? request.getProgress() : 0);
        project.setGithubUrl(request.getGithubUrl());
        project.setPlayUrl(request.getPlayUrl());
        project.setThumbnailUrl(request.getThumbnailUrl());
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());

        GameProject saved = gameProjectRepository.save(project);
        return toDTO(saved);
    }

    // 게임 프로젝트 정보 수정
    public void updateProject(Long id, GameProjectRequestDTO request) {
        GameProject project = findEntity(id);
        project.setTitle(request.getTitle());
        project.setGenre(request.getGenre());
        project.setSummary(request.getSummary());
        project.setStatus(request.getStatus());
        project.setProgress(request.getProgress());
        project.setGithubUrl(request.getGithubUrl());
        project.setPlayUrl(request.getPlayUrl());
        project.setThumbnailUrl(request.getThumbnailUrl());
        project.setUpdatedAt(LocalDateTime.now());
        gameProjectRepository.save(project);
    }

    // 게임 프로젝트 삭제
    public void deleteProject(Long id) {
        GameProject project = findEntity(id);
        gameProjectRepository.delete(project);
    }

    private GameProject findEntity(Long id) {
        return gameProjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트를 찾을 수 없습니다. id=" + id));
    }

    private GameProjectDTO toDTO(GameProject p) {
        return new GameProjectDTO(
                p.getId(),
                p.getTitle(),
                p.getGenre(),
                p.getSummary(),
                p.getStatus(),
                p.getProgress(),
                p.getGithubUrl(),
                p.getPlayUrl(),
                p.getThumbnailUrl(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}
