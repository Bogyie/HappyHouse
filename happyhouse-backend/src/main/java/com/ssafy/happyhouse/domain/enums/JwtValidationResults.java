package com.ssafy.happyhouse.domain.enums;

public enum JwtValidationResults {
    SUCCESS("성공"),
    INVALID("값이 변조된 토큰"),
    EXPIRED("만료된 토큰"),
    UNSUPPORTED("지원하지 않는 토큰"),
    CLAIM_EMPTY("인증 정보 누락"),
    LOGOUT("로그아웃된 토큰 (redis에 남아있지 않음)"),
    NOT_MATCHED("redis에 저장된 토큰의 값과 일치하지 않음")
    ;

    public final String description;

    JwtValidationResults(String description) {
        this.description = description;
    }
}
