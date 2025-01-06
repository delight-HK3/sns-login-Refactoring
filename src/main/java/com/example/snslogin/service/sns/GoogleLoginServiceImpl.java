package com.example.snslogin.service.sns;

import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.snslogin.component.snsloginProperties;
import com.example.snslogin.type.UserType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleLoginServiceImpl implements SnsLoginService{

    private final snsloginProperties snsloginProperties;

    // sns 타입 리턴
    @Override
    public UserType getServiceName() {
        return UserType.GOOGLE;
    }
    
    @Override
    public String getOauthRedirectURL() {

        HashMap<String,Object> params = new HashMap<>();

        params.put("scope",snsloginProperties.getGoogle_access_scope());
        params.put("response_type","code");
        params.put("client_id",snsloginProperties.getGoogle_clientID());
        params.put("redirect_uri",snsloginProperties.getGoogle_callback_Url());
        params.put("access_type", "offline");
        
        String parameterString = params.entrySet().stream()
                .map(x->x.getKey()+"="+x.getValue())
                .collect(Collectors.joining("&"));
        
        String redirectURL = snsloginProperties.getGoogle_Login_Url()+"?"+parameterString;

        return redirectURL;
    }

}