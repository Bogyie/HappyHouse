package com.ssafy.happyhouse.controller.dto.member;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.ssafy.happyhouse.domain.entity.Member;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class MemberSignupDto {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String address;

    @Builder
    public MemberSignupDto(String username, String password, String nickname, String email, String address) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.address = address;
    }

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                     .username(username)
                     .password(passwordEncoder.encode(password))
                     .nickname(nickname)
                     .email(email)
                     .address(address)
                     .isActivate(true)
                     .build();
    }
}
