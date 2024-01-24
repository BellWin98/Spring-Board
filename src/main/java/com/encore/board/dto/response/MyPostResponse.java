package com.encore.board.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPostResponse {
    private Long id;
    private String title;
}
