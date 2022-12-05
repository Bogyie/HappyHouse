package com.ssafy.happyhouse.controller.dto.member;

import lombok.Data;

@Data
public class TokenReissueDto {
    private String accessToken;
    private String refreshToken;
}
