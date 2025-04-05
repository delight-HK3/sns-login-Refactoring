package com.example.snslogin.dto;

import lombok.Builder;
import lombok.Getter;
@Getter
public class userResponse {

    // 유저 정보
    private final int no;                 // 회원번호
    private final String id;              // 아이디
    private final String pwd;             // 비밀번호      
    private final String email;           // 이메일
    private final String nickname;        // 닉네임
    private final String name;            // 이름
    private final String age;             // 나이
    private final String gender;          // 성별
    private final String birthday;        // 생일날짜
    private final String birthyear;       // 생일년도
    private final String mobile;          // 모바일
    private final String mobile_e164;

    @Builder
    public userResponse(int no, String id, String pwd, String email, String nickname
                        , String name, String age, String gender, String birthday, String birthyear
                        , String mobile, String mobile_e164){
        this.no = no;
        this.id = id;
        this.pwd = pwd;
        this.email = email;
        this.nickname = nickname;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.birthday = birthday;
        this.birthyear = birthyear;
        this.mobile = mobile;
        this.mobile_e164 = mobile_e164;
    }
}
