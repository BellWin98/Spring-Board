package com.encore.board.exception.post;

import static com.encore.board.exception.post.PostExceptionList.NOT_FOUND_POST;

public class PostNotExistException extends PostException {
    public PostNotExistException() {
        super(NOT_FOUND_POST.getHttpStatus(),
                NOT_FOUND_POST.getErrorMessage());
    }
}
