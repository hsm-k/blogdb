package com.example.blogdb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLoginRequestDTO {

    // 로그인 form의 name="adminId" 값이 자동으로 들어옵니다.
    private String adminId;

    // 로그인 form의 name="password" 값이 자동으로 들어옵니다.
    private String password;
}
