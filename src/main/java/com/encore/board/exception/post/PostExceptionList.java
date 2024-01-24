package com.encore.board.exception.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostExceptionList {
    NOT_FOUND_POST(HttpStatus.BAD_REQUEST, "해당 ID의 게시글을 찾을 수 없습니다");

    private final HttpStatus httpStatus;
    private final String errorMessage;
}
