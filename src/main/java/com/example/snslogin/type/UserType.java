package com.example.snslogin.type;

import lombok.Getter;

@Getter
public enum UserType {
    NONE("none"), 
    GOOGLE("google"), 
    KAKAO("kakao"), 
    NAVER("naver");

    private final String name;

    UserType(String name){
        this.name = name;
    }
}
