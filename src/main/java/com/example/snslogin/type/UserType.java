package com.example.snslogin.type;

public enum UserType {
    NONE("none"), 
    GOOGLE("google"), 
    KAKAO("kakao"), 
    NAVER("naver");

    private String name;

    UserType(String name){
        this.name = name;
    }
}
