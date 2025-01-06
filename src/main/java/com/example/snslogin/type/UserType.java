package com.example.snslogin.type;

public enum UserType {
    NORMAL("normal"), 
    GOOGLE("google"), 
    KAKAO("kakao"), 
    NAVER("naver");

    private String name;

    UserType(String name){
        this.name = name;
    }
}
