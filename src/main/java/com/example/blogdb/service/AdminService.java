package com.example.blogdb.service;

import com.example.blogdb.Admin;
import com.example.blogdb.dto.AdminLoginRequestDTO;
import com.example.blogdb.dto.AdminSessionDTO;
import com.example.blogdb.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    // 아이디로 관리자를 찾고, 비밀번호까지 일치할 때만 로그인용 정보를 반환합니다.
    // 로그인 실패는 Optional.empty()로 표현합니다.
    public Optional<AdminSessionDTO> login(AdminLoginRequestDTO loginRequest) {
        return adminRepository.findByAdminId(loginRequest.getAdminId())
                .filter(admin -> admin.getPassword().equals(loginRequest.getPassword()))
                .map(this::toSessionDTO);
    }

    // Entity 전체 대신, 화면 표시용 정보만 Session DTO로 바꿉니다.
    // 이때 password는 의도적으로 세션에 넣지 않습니다.
    private AdminSessionDTO toSessionDTO(Admin admin) {
        return new AdminSessionDTO(admin.getId(), admin.getAdminId(), admin.getAdminName());
    }
}
