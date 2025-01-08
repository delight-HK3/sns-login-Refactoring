package com.example.snslogin.service.sns;

import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.example.snslogin.type.UserType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleLoginServiceImpl implements SnsLoginService{

    // sns 로그인 관련 property value
    private final Environment environment;

    // 로그인한 sns 타입 리턴
    @Override
    public UserType getUserType() {
        return UserType.GOOGLE;
    }

    // sns 로그인 form 호출 
    @Override
    public String getRedirectURL() {
        HashMap<String,Object> params = new HashMap<>();

        params.put("scope", environment.getProperty("spring.OAuth2.GOOGLE.scope"));
        params.put("response_type","code");
        params.put("client_id", environment.getProperty("spring.OAuth2.GOOGLE.client-id"));
        params.put("redirect_uri", environment.getProperty("spring.OAuth2.GOOGLE.callback-url"));
        params.put("access_type", "offline");
        
        String parameterString = params.entrySet().stream()
                .map(x->x.getKey()+"="+x.getValue())
                .collect(Collectors.joining("&"));
                
        String redirectURL = environment.getProperty("spring.OAuth2.GOOGLE.url")+"?"+parameterString;

        return redirectURL;
    }

}