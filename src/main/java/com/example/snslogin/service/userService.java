package com.example.snslogin.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.snslogin.service.sns.SnsLoginService;
import com.example.snslogin.type.UserType;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class userService {
    
    private final List<SnsLoginService> loginServices;
    private final HttpServletResponse response;

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
                            .orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
    }
}
