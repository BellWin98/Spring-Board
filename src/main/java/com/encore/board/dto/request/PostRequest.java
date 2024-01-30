package com.encore.board.dto.request;

import lombok.Data;

@Data
public class PostRequest {
    private String title;
    private String contents;
    private String appointment;
    private String appointmentTime;
}
