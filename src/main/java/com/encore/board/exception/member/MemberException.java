package com.encore.board.exception.member;

import com.encore.board.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class MemberException extends ApplicationException {

    protected MemberException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
