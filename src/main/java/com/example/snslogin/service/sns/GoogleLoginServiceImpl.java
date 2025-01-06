package com.example.snslogin.service.sns;

import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.snslogin.component.snsloginProperties;
import com.example.snslogin.fegin.snsAuthApi;
import com.example.snslogin.fegin.snsResurceApi;
import com.example.snslogin.type.UserType;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleLoginServiceImpl implements SnsLoginService{

    // sns 로그인 관련 property value
    private final snsloginProperties snsloginProperties;
    // Authorization 서버와 통신하는 openfegin
    private final snsAuthApi snsAuthApi;
    // Resource 서버와 통신하는 openfegin
    private final snsResurceApi snsResurceApi;

    private final RestTemplate restTemplate = new RestTemplate();

    // sns 타입 확인
    @Override
    public UserType getServiceType() {
        return UserType.GOOGLE;
    }
    
    // sns 로그인 form 호출 
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

    // accesstoken 호출
    @Override
    public String getAccessToken(String authorizationCode) {

        MultiValueMap<String,Object> params = new LinkedMultiValueMap<>();

        params.add("code",authorizationCode);
        params.add("client_id",snsloginProperties.getGoogle_clientID());
        params.add("client_secret",snsloginProperties.getGoogle_secret());
        params.add("redirect_uri",snsloginProperties.getGoogle_callback_Url());
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);

        ResponseEntity<JsonNode> responseNode = restTemplate.exchange("https://oauth2.googleapis.com/token", HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }

    // 로그인 시도한 유저 정보 출력
    @Override
    public JsonNode getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange("https://www.googleapis.com/userinfo/v2/me", HttpMethod.GET, entity, JsonNode.class).getBody();
    }

}