package com.ssafy.happyhouse.controller.dto.member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.ssafy.happyhouse.domain.entity.Role;
import com.ssafy.happyhouse.domain.entity.Member;
import com.ssafy.happyhouse.domain.enums.Authority;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberDetailDto {

    private Long userId;
    private String username;
    private String nickname;
    private String email;
    private String address;
    private List<String> roles;
    private LocalDateTime createDate;
    private boolean isActivate;

    @Builder
    public MemberDetailDto(Long userId, String username, String nickname, String email, String address,
                           LocalDateTime createDate, boolean isActivate, List<String> roles) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.address = address;
        this.createDate = createDate;
        this.isActivate = isActivate;
        this.roles = roles;
    }

    public static MemberDetailDto from(Member entity) {

        final List<String> roles = entity.getRoles()
                                         .stream()
                                         .map(Role::getAuthority)
                                         .map(Authority::name)
                                         .collect(Collectors.toList());

        return builder().userId(entity.getId())
                        .username(entity.getUsername())
                        .nickname(entity.getNickname())
                        .email(entity.getEmail())
                        .address(entity.getAddress())
                        .createDate(entity.getCreatedDate())
                        .isActivate(entity.getIsActivate())
                        .roles(roles)
                        .build();
    }
}
