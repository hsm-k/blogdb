package com.example.blogdb.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

// 비밀번호를 제외한 로그인 관리자 정보만 세션에 저장합니다.
// Serializable은 세션 데이터를 저장하거나 전송할 수 있는 형태로 만드는 표시입니다.
@Getter
@RequiredArgsConstructor
public class AdminSessionDTO implements Serializable {

    private final Long id;
    private final String adminId;
    private final String adminName;
}
