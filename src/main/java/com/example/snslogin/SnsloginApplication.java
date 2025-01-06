package com.example.snslogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SnsloginApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnsloginApplication.class, args);
	}
	
}
