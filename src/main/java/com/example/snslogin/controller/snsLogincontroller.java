package com.example.snslogin.controller;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.snslogin.dto.userResponse;
import com.example.snslogin.service.userService;
import com.example.snslogin.type.UserType;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;

import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@RequiredArgsConstructor
public class snsLogincontroller {
    
    private final userService userService;

    // sns 로그인 관련 property value
    private final Environment properties;
    /**
     * 메인페이지 로딩
     * 
     * @param mav
     * @return
     */
    @GetMapping("/")
    public ModelAndView getMethodName(ModelAndView mav) {

        mav.addObject("googleClientId", properties.getProperty("spring.OAuth2.google.client-id"));
        mav.setViewName("index");
        
        return mav;
    }

    /**
     * SNS 로그인 - Authorization 서버로 부터 유저 소셜 로그인 페이지 출력 
     * 
     * @param userType
     * @throws IOException
     */
    @GetMapping(value = "/auth/{socialLoginType}")
    public void socialLoginType(@PathVariable(name="socialLoginType") UserType userType) {
        
        log.info("form userType : {}", userType);
        userService.requestloginform(userType);
    }

    /**
     * SNS 로그인 - resource 서버로 부터 유저정보 획득
     * 
     * @param userType
     * @param code
     * @return
     */
    @GetMapping(value = "/app/accounts/auth/{socialLoginType}/callback")
    public void getMethodName (
        @PathVariable(name = "socialLoginType") UserType userType, // sns 타입
        @RequestParam(name = "code") String code, // Authorization code
        ServletResponse response) throws IOException {
        
        log.info("resource userType : {}", userType);

        JsonNode jsonResponse = userService.snsLogin(userType, code);
        userResponse userresponse = new userResponse();
        log.info("userResponse : {}", jsonResponse);

        switch (userType) {
            case GOOGLE: { // 구글 sns로그인 출력결과 처리
                userresponse.setId(jsonResponse.get("id").asText());
                userresponse.setEmail(jsonResponse.get("email").asText());
                userresponse.setNickname(jsonResponse.get("name").asText());
                break;
            } case KAKAO: { // 카카오 sns로그인 출력결과 처리
                userresponse.setId(jsonResponse.get("id").asText());
                userresponse.setEmail(jsonResponse.get("kakao_account").get("email").asText());
                userresponse.setNickname(jsonResponse.get("kakao_account").get("profile").get("nickname").asText());
                break;
            } case NAVER: { // 네이버 sns로그인 출력결과 처리
                userresponse.setId(jsonResponse.get("response").get("id").asText());
                userresponse.setEmail(jsonResponse.get("response").get("birthday").asText());
                userresponse.setNickname(jsonResponse.get("response").get("nickname").asText());
                break;
            } default: {
                throw new RuntimeException("NONE SOCIAL TYPE");
            }
        }

        log.info("{} userresponse : {}",userType, userresponse.getId());
        log.info("userresponse : {}", userresponse.getEmail());
        log.info("userresponse : {}", userresponse.getNickname());

        // 세션등을 사용하여 정보를 저장

        response.setContentType("text/html; charset=utf-8");
                    PrintWriter w = response.getWriter();
                    w.write("<script>window.close();</script>");
                    w.flush();
                    w.close();
            
    }
    
}
