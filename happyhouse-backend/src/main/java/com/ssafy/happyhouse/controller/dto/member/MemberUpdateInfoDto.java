package com.ssafy.happyhouse.controller.dto.member;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter @NoArgsConstructor
public class MemberUpdateInfoDto {
    private String nickname;
    private String email;
    private String address;
}
