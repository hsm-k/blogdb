package com.example.blogdb.repository;

import com.example.blogdb.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Spring Data JPA가 adminId를 조건으로 관리자를 찾는 SQL을 자동으로 만듭니다.
    Optional<Admin> findByAdminId(String adminId);
}
