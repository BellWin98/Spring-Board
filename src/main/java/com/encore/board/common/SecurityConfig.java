package com.encore.board.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Spring Security 설정을 커스터마이징
@EnableGlobalMethodSecurity(prePostEnabled = true) // 사전, 사후에 인증/권한 검사 어노테이션 사용 가능
// WebSecurityConfigurerAdapter를 상속하는 방식은 Deprecated(지원 종료) 되었다.
public class SecurityConfig {

    @Bean
    // 스프링 시큐리티에서 기본적으로 제공하는 로그인 화면을 건너뛰고, 내가 만든 로그인 화면으로 튕기도록 커스텀
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // csrf 보안 공격에 대한 설정은 하지 않음 (어차피 RestAPI에서는 csrf공격이 무효화, MVC패턴에서는 해줘야 함)
                .csrf().disable()
                // 특정 URL에 대해 인증 처리 하거나, 인증 처리 안한다는 설정
                // 회원가입, 로그인, Home만 접속 허용
                .authorizeRequests()
                    // 인증 미적용 URL 패턴
                    .antMatchers("/", "/api/members/sign-up", "/api/members/login-page")
                        .permitAll()
                    // 그 외 요청은 모두 인증 필요
                    .anyRequest().authenticated()
                .and()
                // 만약 세션을 사용하지 않으면 아래 내용 설정
                // .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // .and()
                .formLogin()
                    // 인증 대상 URL로 요청이 들어오면 아래 로그인 페이지로 리다이렉트 된다.
                    .loginPage("/api/members/login-page")
                    // 스프링 내장 메서드를 사용하기 위해 /doLogin URL 사용
                    .loginProcessingUrl("/doLogin")
                        .usernameParameter("email")
                        .passwordParameter("pw")
                        .successHandler(new LoginSuccessHandler())
                .and()
                .logout()
                    // Spring Security의 doLogout 기능 그대로 사용
                    .logoutUrl("/doLogout")
                .and()
                .build();
    }
}
