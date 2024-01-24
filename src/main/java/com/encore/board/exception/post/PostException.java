package com.encore.board.exception.post;

import com.encore.board.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class PostException extends ApplicationException {

    protected PostException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
