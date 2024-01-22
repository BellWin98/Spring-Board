package com.encore.board.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberSignUpResponse {
    private String nickname;
}
