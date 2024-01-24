package com.encore.board.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDetailsResponse {
    private Long id;
    private String title;
    private String contents;
    private String createdTime;
}
