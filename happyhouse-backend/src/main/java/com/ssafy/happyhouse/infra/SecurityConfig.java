package com.ssafy.happyhouse.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

//    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SignatureAlgorithm signatureAlgorithm() {
        return SignatureAlgorithm.HS512;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//        final JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider);
//        http
//                // Basic auth 사용 안함
//                .httpBasic().disable()
//                // CSRF 사용 안함
//                .csrf().disable()
//                // JWT를 사용하기 떄문에 세션을 사용하지 않음
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                // 모든 요청을 허가할 경로
//                .antMatchers("/user/login").permitAll()
//                // 위에 언급된 경로 이외의 모든 경로에 인증 필요
//                .anyRequest().authenticated()
//                .and()
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.httpBasic().disable()
            .csrf().disable()
            .authorizeRequests().anyRequest().permitAll();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // FIXME : 테스트
        return NoOpPasswordEncoder.getInstance();
        // 서비스
        // return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
