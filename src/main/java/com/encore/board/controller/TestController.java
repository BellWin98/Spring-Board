package com.encore.board.controller;

import com.encore.board.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j // lombok 내 어노테이션이며, 쉽게 logback 로그 라이브러리 사용 가능
public class TestController {

    // slf4j 어노테이션 사용하지 않고, 직접 라이브러리 import하여 Logger 생성 가능
//    private static final Logger logger = LoggerFactory.getLogger("Logger");

    @Autowired
    private MemberService memberService;

    @GetMapping("/log/test1")
    public String testMethod1(){
        log.debug("Debug 로그");
        log.info("Info 로그");
        log.error("Error 로그");
        return "ok";
    }

    @GetMapping("exception/test1/{id}")
    public String exceptionTestMethod1(@PathVariable Long id){
        memberService.showMemberDetails(id);
        return "ok";
    }

    @GetMapping("userinfo/test")
    public String userInfoTest(HttpServletRequest req){
        // 로그인 유저 정보 얻는 방식
        // 방법1. Session에 Attribute를 통해 접근
        String email1 = req.getSession().getAttribute("email").toString();
        System.out.println("email1: " + email1);

        // 방법2. Session에서 Authentication 객체에 접근
        SecurityContext securityContext = (SecurityContext) req.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        String email2 = securityContext.getAuthentication().getName();
        System.out.println("email2: " + email2);

        // 방법3. SecurityContextHolder에서 Authentication 객체에 접근 (가장 일반적 / 토큰에서 똑같이 사용)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email3 = authentication.getName();
        System.out.println("email3: " + email3);

        return null;
    }
}
