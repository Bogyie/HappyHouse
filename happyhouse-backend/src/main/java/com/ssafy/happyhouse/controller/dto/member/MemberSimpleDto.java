package com.ssafy.happyhouse.controller.dto.member;

import com.ssafy.happyhouse.domain.entity.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MemberSimpleDto {
    private Long userId;
    private String username;
    private String nickname;
    private String email;

    public static MemberSimpleDto fromEntity(Member entity) {
        return builder().userId(entity.getId())
                        .username(entity.getUsername())
                        .nickname(entity.getNickname())
                        .email(entity.getEmail())
                        .build();
    }

    @Builder
    public MemberSimpleDto(Long userId, String username, String nickname, String email) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
    }
}
