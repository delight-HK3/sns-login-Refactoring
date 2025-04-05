package com.example.snslogin.controller;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.snslogin.dto.userResponse;
import com.example.snslogin.service.userService;
import com.example.snslogin.type.UserType;

import jakarta.servlet.ServletResponse;

import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
public class snsLogincontroller {
    
    private final userService userService;
    // sns 로그인 관련 property value
    private final Environment properties;

    public snsLogincontroller(userService userService, Environment properties){
        this.userService = userService;
        this.properties = properties;
    }

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
        userResponse jsonResponse = userService.snsLogin(userType, code);
        log.info("userResponse : {}", jsonResponse);

        response.setContentType("text/html; charset=utf-8");
                    PrintWriter w = response.getWriter();
                    w.write("<script>window.close();</script>");
                    w.flush();
                    w.close();   
    }
    
}
