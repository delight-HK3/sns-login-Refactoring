package com.example.snslogin.service.sns;

import org.springframework.stereotype.Service;

import com.example.snslogin.type.UserType;

@Service
public interface SnsLoginService {
    UserType getServiceName(); // sns 타입 확인
    String getOauthRedirectURL(); // sns 로그인 form 호출
    
    default UserType type(){
        if(this instanceof GoogleLoginServiceImpl){ // 구글 로그인
            return UserType.GOOGLE;
        } 
        else {
            return UserType.NORMAL; // 일반 로그인 방식
        }
    }
} 