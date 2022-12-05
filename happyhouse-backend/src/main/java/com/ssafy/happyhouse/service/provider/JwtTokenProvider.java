package com.ssafy.happyhouse.service.provider;

import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ssafy.happyhouse.controller.protocol.TokenSpec;
import com.ssafy.happyhouse.domain.entity.Role;
import com.ssafy.happyhouse.domain.enums.Authority;
import com.ssafy.happyhouse.domain.enums.JwtValidationResults;
import com.ssafy.happyhouse.service.repository.MemberRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";
    private final Key key;
    private final JwtParser jwtParser;
    private final long accessTokenExpire;
    private final long refreshTokenExpire;
    private final String grantType;
    private final MemberRepository memberRepository;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

    public String createAccessToken(String username, Set<Role> auth) {
        return createToken(username, auth, accessTokenExpire);
    }

    public String createRefreshToken(String username, Set<Role> auth) {
        return createToken(username, auth, refreshTokenExpire);
    }

    public String getUserIdFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return parseClaims(token).get(AUTHORITIES_KEY, String.class);
    }

    public TokenSpec generateTokenSpec(String refreshToken, String accessToken) {
        return TokenSpec.builder()
                        .grantType(grantType)
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
    }

    public Authentication getAuthenticationFromToken(String jwtToken) {
        /*
        - JWS (JSON Web Signature) : 서버에서 인증을 증거로 인증 정보를 서버의 private key 로 서명한 것을 Token 화 한것.
        - JWE (JSON Web Encryption) : 서버와 클라이언트 간 암호화된 데이터를 Token 화 한것.
        - JWK (JSON Web Key) : JWE 에서 payload encryption 에 쓰인 키를 Token 화 한것.
        - JWT (JSON Web Token) : JWS or JWE
         */

        final Claims claims = parseClaims(jwtToken);
        if (claims.get(AUTHORITIES_KEY) == null || !StringUtils.hasText(
                claims.get(AUTHORITIES_KEY).toString())) {
            throw new AuthenticationCredentialsNotFoundException("권한 정보가 없는 토큰 입니다.");
        }

        logger.debug("claims.getAuth = {}", claims.get(AUTHORITIES_KEY));
        logger.debug("claims.getEmail = {}", claims.getSubject());

        // 클레임에서 권한 정보 가져오기
        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                      .map(SimpleGrantedAuthority::new)
                      .collect(Collectors.toList());

        if (logger.isDebugEnabled()) {
            for (GrantedAuthority authority : authorities) {
                logger.debug("getAuthentication -> authorities = {}", authority.getAuthority());
            }
        }

        // UserDetails 객체를 만들어서 Authentication 리턴
        final UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 정보를 검증
    public JwtValidationResults validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return JwtValidationResults.SUCCESS;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("Invalid JWT Token", e);
            return JwtValidationResults.INVALID;
        } catch (ExpiredJwtException e) {
            logger.info("Expired JWT Token", e);
            return JwtValidationResults.EXPIRED;
        } catch (UnsupportedJwtException e) {
            logger.info("Unsupported JWT Token", e);
            return JwtValidationResults.UNSUPPORTED;
        } catch (IllegalArgumentException e) {
            logger.info("JWT claims string is empty.", e);
            return JwtValidationResults.CLAIM_EMPTY;
        }
    }

//    public Boolean verify(
//            String accessToken,
//            String refreshToken,
//            Authority[] ...needAuthorities
//    ) {
//
//        if (validateToken(refreshToken) != JwtValidationResults.SUCCESS) {
//            return false;
//        }
//
//        final Long userId = Long.valueOf(getUserIdFromToken(refreshToken));
//        final Member member = memberRepository.findById(userId).orElseThrow(
//                () -> new IllegalIdentifierException("유저를 찾을 수 없음"));
//
//        final Set<Authority> memberRoles = member.getRoles()
//                                                 .stream()
//                                                 .map(Role::getAuthority)
//                                                 .collect(Collectors.toSet());
//
//        // 요구하는 조건이 없는 경우
//        if (authorities.length == 0) {
//            return true;
//        }
//
//        final Set<Authority> needRoes = Arrays.stream(authorities)
//                                              .collect(Collectors.toSet());
//
//        // ADMIN
//        if (needRoes.contains(ADMIN) && memberRoles.contains(ADMIN)) {
//            return true;
//        }
//
//        // SELF
//        if (needRoes.contains(SELF) && mem)
//
//    }

    JwtTokenProvider(
            @Value("${jwt.secret-base64}") String secret,
            @Value("${jwt.access-token-expire}") long accessTokenExpire,
            @Value("${jwt.refresh-token-expire}") long refreshTokenExpire,
            @Value("${jwt.grant-type}") String grantType,
            MemberRepository memberRepository
    ) {
        final byte[] keyBytes = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpire = accessTokenExpire;
        this.refreshTokenExpire = refreshTokenExpire;
        this.grantType = grantType;
        this.memberRepository = memberRepository;
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    @SuppressWarnings("UseOfObsoleteDateTimeApi")
    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return jwtParser.parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) { // 재 발행을 위해 만료된 토큰이 더라도 일단 파싱을 함
            return e.getClaims();
        }
    }

    private String createToken(String subject, Set<Role> auth, long tokenValidSecond) {
        final String authContent = auth.stream()
                                       .map(Role::getAuthority)
                                       .map(Authority::name)
                                       .collect(Collectors.joining(","));

        final LocalDateTime localDateNow = LocalDateTime.now();
        final LocalDateTime expireLocalDate = localDateNow.plusSeconds(tokenValidSecond);

        return Jwts.builder()
                   .setSubject(subject)
                   .claim(AUTHORITIES_KEY, authContent)
                   .setIssuedAt(localDateTimeToDate(localDateNow))
                   .setExpiration(localDateTimeToDate(expireLocalDate))
                   .signWith(key, signatureAlgorithm)
                   .compact();
    }
}
