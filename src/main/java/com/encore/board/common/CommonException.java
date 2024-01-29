package com.encore.board.common;

import com.encore.board.exception.member.MemberNotExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

// ControllerAdvice와 ExceptionHandler를 통해 예외 처리의 공통화 로직 작성
@ControllerAdvice
@Slf4j
public class CommonException {
    @ExceptionHandler(MemberNotExistException.class)
    public ResponseEntity<Map<String, Object>> entityNotFoundHandler(MemberNotExistException e){
        log.error("Handler MemberNotExistException Message : " + e.getMessage());
        return this.errorResponseMessage(HttpStatus.NOT_FOUND, e.getMessage()); // 404
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> illegalArgumentHandler(IllegalArgumentException e){
        log.error("Handler IllegalArgumentException Message : " + e.getMessage());
        return this.errorResponseMessage(HttpStatus.BAD_REQUEST, e.getMessage()); // 400
    }

    private ResponseEntity<Map<String, Object>> errorResponseMessage(HttpStatus httpStatus, String message){
        // 객체는 JSON으로 직렬화 된다.
        Map<String, Object> body = new HashMap<>();
        body.put("status", String.valueOf(httpStatus.value()));
        body.put("status message", httpStatus.getReasonPhrase());
        body.put("error message", message);
        return new ResponseEntity<>(body, httpStatus);
    }
}
