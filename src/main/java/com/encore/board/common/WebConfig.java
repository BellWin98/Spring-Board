package com.encore.board.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        // 비밀번호 암호화 모듈 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
