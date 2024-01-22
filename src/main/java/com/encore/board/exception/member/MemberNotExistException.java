package com.encore.board.exception.member;

import static com.encore.board.exception.member.MemberExceptionList.NOT_FOUND_MEMBER;

public class MemberNotExistException extends MemberException{
    public MemberNotExistException() {
        super(NOT_FOUND_MEMBER.getHttpStatus(),
                NOT_FOUND_MEMBER.getErrorMessage());
    }
}
