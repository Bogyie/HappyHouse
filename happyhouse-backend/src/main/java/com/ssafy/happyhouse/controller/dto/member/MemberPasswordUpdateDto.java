package com.ssafy.happyhouse.controller.dto.member;

import lombok.Data;

@Data
public class MemberPasswordUpdateDto {
    private String username;
    private String email;
    private String password;
}
