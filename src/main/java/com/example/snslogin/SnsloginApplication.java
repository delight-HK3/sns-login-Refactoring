package com.example.snslogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SnsloginApplication {
	public static void main(String[] args) {
		SpringApplication.run(SnsloginApplication.class, args);
	}

	// RestTemplate Bean 주입
	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
