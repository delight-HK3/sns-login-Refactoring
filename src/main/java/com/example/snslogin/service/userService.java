package com.example.snslogin.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.snslogin.dto.authResponse;
import com.example.snslogin.dto.userResponse;
import com.example.snslogin.service.sns.NormalLoginServiceImpl;
import com.example.snslogin.service.sns.SnsLoginService;
import com.example.snslogin.type.UserType;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class userService {
    
    private final List<SnsLoginService> loginServices;
    private final HttpServletResponse response;

    // Authorization서버에 요청하여 accesstoken 획득 및 accesstoken으로 
    // Resource서버에 요청해 유저 정보 획득
    public JsonNode snsLogin(UserType userType, String code){
        SnsLoginService snsLoginService = this.getLoginService(userType);
        JsonNode useresponse;

        // 일반로그인시 accesstoken은 별도로 발급 받을 수 없기에 DB의 유저정보 조회로 이동
        if(snsLoginService.getServiceType().equals(userType.NORMAL)){
            useresponse = snsLoginService.getUserInfo(code);
        } else {
            String accessCode = snsLoginService.getAccessToken(code);
            useresponse = snsLoginService.getUserInfo(accessCode);
        }

        return useresponse;
    }

    // sns로그인 시도시 Authorization서버에서 제공하는 페이지 호출
    public void requestloginform(UserType snsType){
        SnsLoginService snsloginservice = this.findSnsType(snsType); 
        String redirectURL = snsloginservice.getOauthRedirectURL();

        try{    
            // 각 sns로그인 관련 SNS service에서 생성한 호출 URL 호출
            response.sendRedirect(redirectURL); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // sns 타입 확인
    private SnsLoginService findSnsType(UserType userType){
        return loginServices.stream()
                            .filter(x -> x.type().equals(userType))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("알 수 없는 userType 입니다."));
    }

    private SnsLoginService getLoginService(UserType userType){
        for (SnsLoginService loginService: loginServices) {
            if (userType.equals(loginService.getServiceType())) {
                log.info("login service Type: {}", loginService.getServiceType());
                return loginService;
            }
        }
        return new NormalLoginServiceImpl();
    }
}
