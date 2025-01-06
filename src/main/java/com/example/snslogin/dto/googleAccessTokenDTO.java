package com.example.snslogin.dto;

import lombok.Builder;

@Builder
public class googleAccessTokenDTO {
    private String code;            // access token
    private String client_id;       // 클라이언트 아이디
    private String clientSecret;    // 시크릿 키
    private String redirect_uri;    // google cloud 플랫폼에 등록한 callback url
    
    @Builder.Default
    private String grant_type = "authorization_code";   
    // 인가 코드 방식을 기본으로 authorization_code 디폴트값값 설정
}
