package com.encore.board.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
@Aspect
@Slf4j
public class AopLogService {

    // AOP의 대상이 되는 Controller, Service 등을 정의
    // 패키지, 메서드명의 표현식
//    @Pointcut("execution(* com.encore.board..controller..*.*(..)")
    @Pointcut("within(@org.springframework.stereotype.Controller *)") // 어노테이션 표현식
//    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)") // CSR에서는 RestController 사용)
    public void controllerPointcut(){

    }

    // 방식 1: @Before, @After 사용
//    @Before("controllerPointcut()")
//    public void beforeController(JoinPoint joinPoint){
//        log.info("Before Controller");
//        // @Around의 메서드가 실행되기 전에 인증, 입력값 검증 등을 수행하는 용도로 사용하는 단계
//        ServletRequestAttributes servletRequestAttributes =
//                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest req = servletRequestAttributes.getRequest();
//
//        // JSON 형태로 사용자의 요청을 조립하기 위한 로직
//        ObjectMapper objectMapper = new ObjectMapper();
//        ObjectNode objectNode = objectMapper.createObjectNode();
//        objectNode.put("Method Name", joinPoint.getSignature().getName());
//        objectNode.put("CRUD Name", req.getMethod());
//        Map<String, String[]> paramMap = req.getParameterMap();
//        JsonNode jsonNode = objectMapper.valueToTree(paramMap);
//        objectNode.set("User Inputs", jsonNode);
//
//        log.info("User Request Info" + objectNode);
//    }
//
//    @After("controllerPointcut()")
//    public void afterController(){
//        log.info("End Controller");
//    }

    // 방식 2: @Around 사용 (가장 빈번히 사용)
    @Around("controllerPointcut()")
    // join point란, AOP 대상으로 하는 Controller의 특정 메서드를 의미
    // 사용자 요청을 중간에서 낚아채서 필요한 로그 출력 후 원래 컨트롤러로 돌려 보냄
    // proceedingJoinPoint에 사용자의 요청 정보가 담겨 있음
    public Object controllerLogger(ProceedingJoinPoint proceedingJoinPoint){
//        log.info("Request Method: " + proceedingJoinPoint.getSignature().toString());

        // 여기서부터 사전 작업
        // 사용자의 요청 값을 출력하기 위해 HttpServletRequest 객체를 꺼내는 로직
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = servletRequestAttributes.getRequest();

        // JSON 형태로 사용자의 요청을 조립하기 위한 로직
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("Method Name", proceedingJoinPoint.getSignature().getName());
        objectNode.put("CRUD Name", req.getMethod());
        Map<String, String[]> paramMap = req.getParameterMap();
        JsonNode jsonNode = objectMapper.valueToTree(paramMap);
        objectNode.set("User Inputs", jsonNode);

        log.info("User Request Info" + objectNode);

        try {
            // 본래의 컨트롤러 메서드 호출하는 부분
            return proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            log.info("End Controller"); // 이후 작업
        }
    }
}
