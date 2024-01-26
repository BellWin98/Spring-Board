package com.encore.board.exception.member;

import static com.encore.board.exception.member.MemberExceptionList.EXIST_TRUE_MEMBER_EMAIL;

public class MemberEmailAlreadyExistException extends MemberException{
    public MemberEmailAlreadyExistException() {
        super(EXIST_TRUE_MEMBER_EMAIL.getHttpStatus(),
                EXIST_TRUE_MEMBER_EMAIL.getErrorMessage());
    }
}
