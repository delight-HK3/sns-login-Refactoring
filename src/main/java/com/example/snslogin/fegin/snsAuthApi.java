package com.example.snslogin.fegin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.snslogin.config.FeignConfiguration;
import com.example.snslogin.dto.googleAccessTokenDTO;

@FeignClient(value = "snsAuth", configuration = {FeignConfiguration.class})
public interface snsAuthApi {
    
    @PostMapping("https://oauth2.googleapis.com/token") // 구글 accesstoken 호출
    ResponseEntity<String> googlegetAccessToken(@RequestBody googleAccessTokenDTO requestDto);

    @GetMapping("https://nid.naver.com/oauth2.0/token") // 네이버 accesstoken 호출
    ResponseEntity<String> navergetAccessToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("code") String code,
            @RequestParam("state") String state
    );

    @GetMapping("https://kauth.kakao.com/oauth/token")
    ResponseEntity<String> kakaogetAccessToken( // 카카오 accesstoken 호출
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("grant_type") String grantType,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("code") String authorizationCode
    );
}
