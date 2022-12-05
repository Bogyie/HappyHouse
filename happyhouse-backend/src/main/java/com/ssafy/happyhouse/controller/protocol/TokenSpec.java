package com.ssafy.happyhouse.controller.protocol;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenSpec {

    private String grantType;
    private String accessToken;
    private String refreshToken;

    @Builder
    public TokenSpec(String grantType, String accessToken, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
