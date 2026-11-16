package com.example.blogdb;

import com.example.blogdb.repository.AdminRepository;
import com.example.blogdb.repository.GameProjectRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class BlogdbApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogdbApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(AdminRepository adminRepository, GameProjectRepository gameProjectRepository) {
        return args -> {
            // 앱 실행 시 기본 관리자 계정이 없으면 자동 생성
            if (adminRepository.findByAdminId("admin").isEmpty()) {
                Admin admin = new Admin();
                admin.setAdminId("admin");
                admin.setPassword("1234");
                admin.setAdminName("기본 관리자");
                admin.setCreatedAt(LocalDateTime.now());
                adminRepository.save(admin);
            }

            // 게임개발 블로그를 위한 초기 샘플 게임 프로젝트 생성 (등록된 프로젝트가 없을 때)
            if (gameProjectRepository.count() == 0) {
                GameProject p1 = new GameProject();
                p1.setTitle("마블 벤치 (Marble Bench)");
                p1.setGenre("3D 물리 액션 어드벤처");
                p1.setStatus("개발 중");
                p1.setProgress(75);
                p1.setSummary("구슬의 물리 가속도와 경사면 트랙을 조작하여 목표 지점에 도달하는 3D 물리 기반 게임입니다. Unity 엔진과 모듈형 타일맵을 적용했습니다.");
                p1.setGithubUrl("https://github.com/example/marble-bench");
                p1.setPlayUrl("https://play.unity.com");
                p1.setCreatedAt(LocalDateTime.now().minusDays(5));
                p1.setUpdatedAt(LocalDateTime.now().minusDays(1));
                gameProjectRepository.save(p1);

                GameProject p2 = new GameProject();
                p2.setTitle("픽셀 템플 크롤러 (Modular Temple)");
                p2.setGenre("2D 로그라이크 RPG");
                p2.setStatus("기획 완료");
                p2.setProgress(35);
                p2.setSummary("고대 유적을 탐험하며 절차적으로 생성되는 방을 돌파하는 2D 픽셀 액션 게임입니다. 타일 기반 이동과 인벤토리 시스템을 제작 중입니다.");
                p2.setGithubUrl("https://github.com/example/modular-temple");
                p2.setCreatedAt(LocalDateTime.now().minusDays(10));
                p2.setUpdatedAt(LocalDateTime.now().minusDays(3));
                gameProjectRepository.save(p2);
            }
        };
    }
}
