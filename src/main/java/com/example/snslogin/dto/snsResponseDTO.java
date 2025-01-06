package com.example.snslogin.dto;

import lombok.Getter;

@Getter
public class snsResponseDTO {
    private String snsType;         // 로그인 한 sns 타입
    private String accessToken;     // accessToken 값

    // 유저 정보
    private int no;                // 회원번호
    private String id;              // 아이디
    private String pwd;             // 비밀번호      
    private String email;           // 이메일
    private String nickname;        // 닉네임
    private String name;            // 이름
    private String age;             // 나이
    private String gender;          // 성별
    private String birthday;        // 생일날짜
    private String birthyear;       // 생일년도
    private String mobile;          // 모바일
    private String mobile_e164;
}
