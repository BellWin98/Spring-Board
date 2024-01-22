package com.encore.board.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberSimpleInfoResponse {
    private Long id;
    private String nickname;
    private String email;
}
