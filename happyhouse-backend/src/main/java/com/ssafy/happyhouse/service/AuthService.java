package com.ssafy.happyhouse.service;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.happyhouse.controller.dto.member.MemberLoginDto;
import com.ssafy.happyhouse.controller.dto.member.TokenReissueDto;
import com.ssafy.happyhouse.controller.protocol.TokenSpec;
import com.ssafy.happyhouse.domain.entity.Member;
import com.ssafy.happyhouse.domain.entity.RefreshToken;
import com.ssafy.happyhouse.domain.entity.Role;
import com.ssafy.happyhouse.domain.enums.Authority;
import com.ssafy.happyhouse.domain.enums.JwtValidationResults;
import com.ssafy.happyhouse.service.exception.AuthException;
import com.ssafy.happyhouse.service.provider.JwtTokenProvider;
import com.ssafy.happyhouse.service.repository.MemberRepository;
import com.ssafy.happyhouse.service.repository.RefreshTokenRepository;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * @param loginDto 로그인 요청 정보
     * @throws NoSuchElementException 멤버를 찾을 수 없음
     */
    public TokenSpec login(MemberLoginDto loginDto) throws IllegalIdentifierException {
        final Member member = memberRepository.findMemberByUsername(loginDto.getUsername()).orElseThrow();

        final String memberPassword = member.getPassword();
        final String loginPassword = passwordEncoder.encode(loginDto.getPassword());
        if (!memberPassword.equals(loginPassword)) {
            throw new IllegalIdentifierException("유저 아이디와 비밀번호가 일치하지 않음");
        }

        final Long userId = member.getId();
        final Set<Role> authorities = member.getRoles();
        final String accessToken = jwtTokenProvider.createAccessToken(userId.toString(), authorities);
        final String refreshToken = jwtTokenProvider.createRefreshToken(userId.toString(), authorities);

        refreshTokenRepository.save(RefreshToken.builder()
                                                .userId(userId)
                                                .token(refreshToken)
                                                .build());

        return jwtTokenProvider.generateTokenSpec(refreshToken, accessToken);
    }

    /**
     * @param tokenReissueDto 토큰 재발행 요청 정보
     * @throws AuthException 인증 오류
     */
    public TokenSpec reissue(TokenReissueDto tokenReissueDto) {

        final String originAccessToken = tokenReissueDto.getAccessToken();
        final String originRefreshToken = tokenReissueDto.getRefreshToken();

        final JwtValidationResults result = jwtTokenProvider.validateToken(originRefreshToken);
        if (result != JwtValidationResults.SUCCESS) {
            throw new AuthException(result);
        }

        final Authentication authentication = jwtTokenProvider.getAuthenticationFromToken(originAccessToken);
        logger.debug("Authentication = {}", authentication);

        final RefreshToken refreshToken = refreshTokenRepository.findById(authentication.getName())
                                                                .orElseThrow(() -> new AuthException(JwtValidationResults.LOGOUT));

        if (!refreshToken.getToken().equals(originRefreshToken)) {
            throw new AuthException(JwtValidationResults.NOT_MATCHED);
        }

        final String username = jwtTokenProvider.getUserIdFromToken(originAccessToken);
        final Member member = memberRepository.findMemberByUsername(username).orElseThrow();

        final Set<Role> authorities = member.getRoles();
        final String newAccessToken = jwtTokenProvider.createAccessToken(username, authorities);
        final String newRefreshToken = jwtTokenProvider.createRefreshToken(username, authorities);
        final TokenSpec tokenSpec = jwtTokenProvider.generateTokenSpec(newRefreshToken, newAccessToken);

        logger.debug("refresh Origin = {}", originRefreshToken);
        logger.debug("refresh New = {}", newRefreshToken);

        refreshToken.updateToken(newRefreshToken);
        return tokenSpec;
    }

    public boolean verifySelfOrAdmin(String accessToken, Long resourceId) {
        final Long userIdFromToken = Long.valueOf(jwtTokenProvider.getUserIdFromToken(accessToken));
        final Member member = memberRepository.findById(userIdFromToken).orElseThrow();

        Set<Authority> memberHasAuthorities = member.getRoles()
                                                    .stream()
                                                    .map(Role::getAuthority)
                                                    .collect(Collectors.toSet());

        if (memberHasAuthorities.contains(Authority.ADMIN)) {
            return true;
        }

        final Long memberId = member.getId();
        if (memberHasAuthorities.contains(Authority.USER) && resourceId.equals(memberId)) {
            return true;
        }

        return false;
    }

    @Autowired
    public AuthService(
            MemberRepository memberRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.memberRepository = memberRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }
}
