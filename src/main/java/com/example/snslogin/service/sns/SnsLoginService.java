package com.example.snslogin.service.sns;

import javax.imageio.IIOException;

import org.springframework.stereotype.Service;

import com.example.snslogin.type.UserType;

@Service
public interface SnsLoginService {
    String getRedirectURL(); // sns 로그인 form 호출
    
    default UserType type(){
        if(this instanceof GoogleLoginServiceImpl){ 
            return UserType.GOOGLE; // 구글 로그인
        } else if(this instanceof KakaoLoginServiceImpl){ 
            return UserType.KAKAO; // 카카오 로그인
        } else if(this instanceof NaverLoginServiceImpl){ 
            return UserType.NAVER; // 네이버 로그인
        } else{
            return UserType.NONE; // 존재하지 않는 sns 타입
        }
    }
} 