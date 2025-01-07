package com.example.snslogin.service;

import java.io.IOException;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
    // sns 로그인 관련 property value
    private final Environment environment;
    private final RestTemplate restTemplate = new RestTemplate();

    // Authorization서버에 요청하여 accesstoken 획득 및 accesstoken으로 
    // Resource서버에 요청해 유저 정보 획득
    public JsonNode snsLogin(UserType userType, String code){
        
        String accessToken = this.getAccessToken(userType, code);
        JsonNode useresponse = this.getUserInfo(userType, accessToken);
        
        System.out.println("useresponse "+userType+" : "+useresponse);

        return useresponse;
    } 

    // sns로그인 시도시 Authorization서버에서 제공하는 페이지 호출
    // 일반로그인은 내부에서 처리
    public void requestloginform(UserType snsType){
        SnsLoginService snsloginservice = this.findSnsType(snsType); 
        String redirectURL = snsloginservice.getRedirectURL();
        
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

    // Authorization 서버를 통해 accesstoken 발급
    private String getAccessToken(UserType userType, String authorizationCode){
        MultiValueMap<String,Object> params = new LinkedMultiValueMap<>();

        // 각 sns별 Authorization 서버주소
        String accesstokenUrl = environment.getProperty("spring.OAuth2."+userType+".Authorization-url");

        params.add("code",authorizationCode);
        params.add("client_id", environment.getProperty("spring.OAuth2."+userType+".client-id"));
        params.add("client_secret",environment.getProperty("spring.OAuth2."+userType+".client-secret"));
        params.add("redirect_uri", environment.getProperty("spring.OAuth2."+userType+".callback-url"));
        params.add("grant_type", "authorization_code"); 

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);

        ResponseEntity<JsonNode> responseNode = 
            restTemplate.exchange(accesstokenUrl, HttpMethod.POST, entity, JsonNode.class);
        
        JsonNode accessTokenNode = responseNode.getBody();
        
        return accessTokenNode.get("access_token").asText();
    }

    // Resource 서버를 통해 유저정보 발급
    private JsonNode getUserInfo(UserType userType, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        String resourceUrl = environment.getProperty("spring.OAuth2."+userType+".resource-url");

        HttpEntity entity = new HttpEntity(headers);

        return restTemplate.exchange(resourceUrl, HttpMethod.GET, entity, JsonNode.class).getBody();
    }

}
