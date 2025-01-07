package com.example.snslogin.service.sns;

import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NaverLoginServiceImpl implements SnsLoginService{
    
    // sns 로그인 관련 property value
    private final Environment environment;

    // sns 로그인 form 호출 
    @Override   
    public String getRedirectURL() {
        HashMap<String,Object> params = new HashMap<>();

        params.put("response_type","code");
        params.put("client_id",environment.getProperty("spring.OAuth2.NAVER.client-id"));
        params.put("redirect_uri",environment.getProperty("spring.OAuth2.NAVER.callback-url"));
        params.put("state",environment.getProperty("spring.OAuth2.NAVER.state"));
        
        String parameterString = params.entrySet().stream()
                .map(x->x.getKey()+"="+x.getValue())
                .collect(Collectors.joining("&"));

        String redirectURL = environment.getProperty("spring.OAuth2.NAVER.url")+"?"+parameterString;

        return redirectURL;
    }
    
}