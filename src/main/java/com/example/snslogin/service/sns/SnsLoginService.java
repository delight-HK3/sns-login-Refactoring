package com.example.snslogin.service.sns;

import org.springframework.stereotype.Service;

import com.example.snslogin.dto.authResponse;
import com.example.snslogin.dto.userResponse;
import com.example.snslogin.type.UserType;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public interface SnsLoginService {
    UserType getServiceType(); // sns 타입 확인
    String getOauthRedirectURL(); // sns 로그인 form 호출
    String getAccessToken(String authorizationCode); // accesstoken 호출
    JsonNode getUserInfo(String accessToken); // 로그인 시도한 유저 정보 출력

    default UserType type(){
        if(this instanceof GoogleLoginServiceImpl){ // 구글 로그인
            return UserType.GOOGLE;
        } 
        else {
            return UserType.NORMAL; // 일반 로그인 방식
        }
    }
} 