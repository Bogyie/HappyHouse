package com.ssafy.happyhouse.domain.entity;

import static org.springframework.util.StringUtils.hasText;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ssafy.happyhouse.controller.dto.member.MemberUpdateInfoDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "MEMBER")
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert  // `insert` 시 `null`인 필드 제외
@DynamicUpdate  // `update` 시 `null`인 필드 제외
@Getter
@Setter
@NoArgsConstructor
public class Member extends BaseTime implements Serializable, UserDetails {

    private static final long serialVersionUID = -1571133348731349969L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME", updatable = false, unique = true)
    private String username; // 로그인 아이디

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "ACTIVATE")
    private Boolean isActivate;

    @CreatedDate
    @Column(name = "PASSWORD_UPDATE_DATE")
    private LocalDateTime passwordUpdateDate;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany
    // 다대다 매핑 테이블 정의
    @JoinTable(
            name = "MEMBER_AUTHORITY",
            joinColumns = @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")
    )
    private Set<Role> roles = new HashSet<>();

    public Set<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(Role::getAuthority).collect(Collectors.toSet());
    }

    public Member updateInfoFrom(MemberUpdateInfoDto updateDto) {

        // nickname
        final String nickname = updateDto.getNickname();
        if (hasText(nickname)) {
            this.nickname = nickname;
        }

        // email
        final String email = updateDto.getEmail();
        if (hasText(email)) {
            this.email = email;
        }


        // address
        final String address = updateDto.getAddress();
        if (hasText(address)) {
            this.address = address;
        }

        return this;
    }

    public void addAuthority(Role role) {
        roles.add(role);
    }

    public void removeAuthority(Role role) {
        roles.remove(role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActivate;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActivate;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActivate;
    }

    @Override
    public boolean isEnabled() {
        return isActivate;
    }

    @Builder
    public Member(String username, String password, String email, Boolean isActivate, String nickname,
                  String address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isActivate = isActivate;
        this.nickname = nickname;
        this.address = address;
    }
}
