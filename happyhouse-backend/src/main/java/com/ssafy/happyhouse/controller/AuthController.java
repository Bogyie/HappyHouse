package com.ssafy.happyhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.happyhouse.service.AuthService;
import com.ssafy.happyhouse.service.provider.JwtTokenProvider;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    public ResponseEntity<?> jwtTokenVerify(
            @RequestParam String accessToken,
            @RequestParam String refreshToken
    ) {
        String subject = jwtTokenProvider.getUserIdFromToken(accessToken);
        return ResponseEntity.ok(subject);
    }

    @Autowired
    public AuthController(
            AuthService authService,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }
}
