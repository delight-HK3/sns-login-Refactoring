package com.example.snslogin.fegin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.snslogin.config.FeignConfiguration;

// resource 서버와 통신
@FeignClient(value = "snsResurce", url="https://", configuration = {FeignConfiguration.class})
public interface snsResurceApi {

    @GetMapping("www.googleapis.com/userinfo/v2/me") // 구글로부터 유저정보 리턴
    ResponseEntity<String> googleGetUserInfo(@RequestParam("access_token") String accessToken);

    @GetMapping("openapi.naver.com/v1/nid/me") // 네이버로부터 유저정보 리턴
    ResponseEntity<String> naverGetUserInfo(@RequestHeader Map<String, String> header);

    @GetMapping("kapi.kakao.com/v2/user/me") // 카카오로부터 유저정보 리턴
    ResponseEntity<String> kakaoGetUserInfo(@RequestHeader Map<String, String> header);
} 
