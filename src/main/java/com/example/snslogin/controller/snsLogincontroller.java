package com.example.snslogin.controller;

import java.io.IOException;

import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.snslogin.component.snsloginProperties;
import com.example.snslogin.service.userService;
import com.example.snslogin.type.UserType;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@RequiredArgsConstructor
public class snsLogincontroller {
    
    private final userService userService;
    private final snsloginProperties snsloginProperties;

    /**
     * 메인페이지 로딩
     * 
     * @param mav
     * @return
     */
    @GetMapping("/")
    public ModelAndView getMethodName(ModelAndView mav) {

        mav.addObject("googleClientId", snsloginProperties.getGoogle_clientID());
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
    public void socialLoginType(@PathVariable(name="socialLoginType") UserType snsType) throws IOException {
        
        log.info("sns Type", snsType);
        userService.requestloginform(snsType);
    }

    
    @GetMapping(value = "/app/accounts/auth/{socialLoginType}/callback")
    public String getMethodName(
        @PathVariable(name = "socialLoginType") UserType userType, // sns 타입
        @RequestParam(name = "code") String code // Authorization code
        ) {
            

        return new String();
    }
    
}
