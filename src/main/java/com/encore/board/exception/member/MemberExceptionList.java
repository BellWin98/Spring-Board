package com.encore.board.exception.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberExceptionList {
    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "해당 ID의 회원을 찾을 수 없습니다"),
    EXIST_TRUE_MEMBER_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    EXIST_TRUE_MEMBER_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;
}
