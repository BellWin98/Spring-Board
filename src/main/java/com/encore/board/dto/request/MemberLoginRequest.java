package com.encore.board.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class MemberLoginRequest {
    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "올바른 이메일주소를 입력하세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력하세요.")
    private String password;
}
