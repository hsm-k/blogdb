package com.example.blogdb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Admin {

    // DB 테이블의 기본 키입니다. 값은 DB가 자동으로 생성합니다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 로그인할 때 사용하는 관리자 아이디입니다.
    // DB의 기존 login_id 컬럼과 연결합니다.
    @Column(name = "login_id", nullable = false, unique = true)
    @Setter
    private String adminId;

    // 로그인 검증에 사용하는 비밀번호입니다. (실제 서비스에서는 암호화해서 저장해야 합니다.)
    @Column(nullable = false)
    @Setter
    private String password;

    // 관리자 페이지에서 환영 문구 등에 표시할 이름입니다.
    @Column(name = "name", nullable = false)
    @Setter
    private String adminName;

    // 계정이 만들어진 시간을 저장합니다.
    @Column(nullable = false)
    @Setter
    private LocalDateTime createdAt;
}
