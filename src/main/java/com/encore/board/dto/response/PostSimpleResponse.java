package com.encore.board.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostSimpleResponse {
    private Long id;
    private String title;
    private String memberEmail;
}
