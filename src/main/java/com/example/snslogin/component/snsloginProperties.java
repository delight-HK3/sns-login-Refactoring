package com.example.snslogin.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class snsloginProperties {
    
    // naver sns login properties
    @Value("${spring.Oauth2.Naver.url}")
    private String naver_Login_Url; // 네이버 Authorization서버로부터 로그인form 호출 Url

    @Value("${spring.OAuth2.Naver.client-id}")
    private String naver_clientID; // 네이버 로그인 클라이언트 아이디

    @Value("${spring.OAuth2.Naver.client-secret}")
    private String naver_secret; // 네이버 로그인 비밀키

    @Value("${spring.OAuth2.Naver.callback-url}")
    private String naver_callback_Url; // 네이버 로그인 resource server 호출 url


    // kakao sns login properties
    @Value("${spring.Oauth2.Kakao.url}")
    private String kakao_Login_Url; // 카카오 Authorization서버로부터 로그인form 호출 Url

    @Value("${spring.Oauth2.Kakao.client-id}")
    private String kakao_clientID; // 카카오 로그인 클라이언트 아이디

    @Value("${spring.OAuth2.Kakao.scope}") 
    private String kakao_access_scope; // 카카오 로그인 시도시 서비스 동의 목록

    @Value("${spring.Oauth2.Kakao.callback-url}")
    private String kakao_callback_Url; // 카카오 로그인 resource server 호출 url
    

    // Google sns login properties
    @Value("${spring.OAuth2.google.url}")
    private String google_Login_Url; // 구글 Authorization서버로부터 로그인form 호출 Url

    @Value("${spring.OAuth2.google.client-id}")
    private String google_clientID; // 구글 로그인 클라이언트 아이디

    @Value("${spring.OAuth2.google.client-secret}")
    private String google_secret; // 구글 로그인 비밀키

    @Value("${spring.OAuth2.google.callback-url}")
    private String google_callback_Url; // 구글 로그인 resource server 호출 url

    @Value("${spring.OAuth2.google.scope}")
    private String google_access_scope; // 구글 로그인 시도시 서비스 동의 목록

    @Value("${spring.OAuth2.google.token.url}")
    private String google_token_url; // 구글 로그인 토큰 요청 url

    @Value("${spring.OAuth2.google.token.user}")
    private String google_access_token_url; // 구글 로그인 access 토큰 요청 url
}
