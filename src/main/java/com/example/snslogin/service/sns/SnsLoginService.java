package com.example.snslogin.service.sns;

import org.springframework.stereotype.Service;

import com.example.snslogin.type.UserType;

@Service
public interface SnsLoginService {
    String getRedirectURL(); // sns 로그인 form 호출
    
    UserType getUserType(); // 로그인한 sns 타입 리턴
} 