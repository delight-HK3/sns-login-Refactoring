package com.example.snslogin.service;

import java.io.IOException;

import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.example.snslogin.dto.userResponse;
import com.example.snslogin.service.sns.SnsLoginService;
import com.example.snslogin.type.UserType;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class userService {
    
    private final List<SnsLoginService> loginServices;
    private final HttpServletResponse response;
    private final Environment environment; // propertyies파일 value
    private final RestClient restClient;

    public userService(List<SnsLoginService> loginServices, HttpServletResponse response
                        , Environment environment, RestClient.Builder restClientBuilder){
        this.loginServices = loginServices;
        this.response = response;
        this.environment = environment;
        this.restClient = restClientBuilder.build(); // restClient 기본 설정
    }

    // Authorization서버에 요청하여 accesstoken 획득 및 accesstoken으로 
    // Resource서버에 요청해 유저 정보 획득
    public userResponse snsLogin(UserType snsType, String code){
        SnsLoginService snsloginservice = this.findSnsType(snsType); 
        
        String accessToken = this.getAccessToken(snsType, code);
        JsonNode jsonNode = this.getUserInfo(snsType, accessToken);

        userResponse result = snsloginservice.getResponse(jsonNode);

        return result;
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
                            .filter(x -> x.getUserType().equals(userType))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("알 수 없는 userType 입니다."));
    }

    // Authorization 서버를 통해 accesstoken 발급
    private String getAccessToken(UserType userType, String authorizationCode){
        // HTTP 파라미터, 폼 데이터 등 다중 값 처리 용도
        MultiValueMap<String,Object> params = new LinkedMultiValueMap<>();

        // 각 sns별 Authorization 서버주소
        String accesstokenUrl = environment.getProperty("spring.OAuth2."+userType+".Authorization-url");

        params.add("code",authorizationCode);
        params.add("client_id", environment.getProperty("spring.OAuth2."+userType+".client-id"));
        params.add("client_secret",environment.getProperty("spring.OAuth2."+userType+".client-secret"));
        params.add("redirect_uri", environment.getProperty("spring.OAuth2."+userType+".callback-url"));
        params.add("grant_type", "authorization_code"); 

        ResponseEntity<JsonNode> responseNode = restClient.post()
                                .uri(accesstokenUrl)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .body(params)
                                .retrieve()
                                .toEntity(JsonNode.class);

        JsonNode accessTokenNode = responseNode.getBody();
        
        return accessTokenNode.get("access_token").asText();
    }

    // Resource 서버를 통해 유저정보 발급
    private JsonNode getUserInfo(UserType userType, String accessToken) {
       
        String resourceUrl = environment.getProperty("spring.OAuth2."+userType+".resource-url");

        JsonNode result = restClient.get()
                                .uri(resourceUrl)
                                .header("Authorization",  "Bearer " + accessToken)
                                .retrieve()
                                .body(JsonNode.class);
                                
        return result;
    }

}
