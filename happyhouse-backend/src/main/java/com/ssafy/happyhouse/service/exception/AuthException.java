package com.ssafy.happyhouse.service.exception;

import com.ssafy.happyhouse.domain.enums.JwtValidationResults;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {

    private JwtValidationResults results;

    public AuthException(JwtValidationResults results) {
        super(results.name());
        this.results = results;
    }
}
