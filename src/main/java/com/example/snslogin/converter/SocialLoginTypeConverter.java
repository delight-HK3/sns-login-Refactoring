package com.example.snslogin.converter;

import com.example.snslogin.type.UserType;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class SocialLoginTypeConverter implements Converter<String, UserType> {
    @Override
    public UserType convert(@SuppressWarnings("null") String s) {
        // 입력받은 문자열을 모두 대문자로 변경
        // SNS 로그인시 어떤 SNS로 했는지 Enum으로 구분하는데
        // Enum은 대문자만 적용이 된다.
        return UserType.valueOf(s.toUpperCase());
    }
}
