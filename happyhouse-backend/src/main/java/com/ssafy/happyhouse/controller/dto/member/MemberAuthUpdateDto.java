package com.ssafy.happyhouse.controller.dto.member;

import java.util.List;

import lombok.Data;

@Data
public class MemberAuthUpdateDto {
    List<String> roles;
}
