package com.encore.board.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j // lombok 내 어노테이션이며, 쉽게 logback 로그 라이브러리 사용 가능
public class LogTestController {

    // slf4j 어노테이션 사용하지 않고, 직접 라이브러리 import하여 Logger 생성 가능
//    private static final Logger logger = LoggerFactory.getLogger("Logger");
    @GetMapping("/log/test1")
    public String testMethod1(){
        log.debug("Debug 로그");
        log.info("Info 로그");
        log.error("Error 로그");
        return "ok";
    }
}
