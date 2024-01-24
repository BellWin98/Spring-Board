package com.encore.board.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberDetailsResponse {
    private Long id;
    private String nickname;
    private String email;
    private String password;
    private String createdTime;
    private int postCount;
    private String role;
}
