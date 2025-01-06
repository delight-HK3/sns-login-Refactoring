package com.example.snslogin.service.sns;

import org.springframework.stereotype.Service;

import com.example.snslogin.dto.authResponse;
import com.example.snslogin.dto.userResponse;
import com.example.snslogin.type.UserType;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class NormalLoginServiceImpl implements SnsLoginService{

    @Override
    public UserType getServiceType() {
        return UserType.NORMAL;
    }

    @Override
    public String getOauthRedirectURL() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOauthRedirectURL'");
    }

    @Override
    public String getAccessToken(String authorizationCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAccessToken'");
    }

    @Override
    public JsonNode getUserInfo(String accessToken) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserInfo'");
    }
    
}
