package com.ssafy.happyhouse.service;

import static org.springframework.util.StringUtils.hasText;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.happyhouse.controller.dto.member.MemberLoginDto;
import com.ssafy.happyhouse.controller.dto.member.MemberSignupDto;
import com.ssafy.happyhouse.controller.dto.member.TokenReissueDto;
import com.ssafy.happyhouse.controller.dto.member.MemberUpdateInfoDto;
import com.ssafy.happyhouse.controller.protocol.TokenSpec;
import com.ssafy.happyhouse.domain.entity.Article;
import com.ssafy.happyhouse.domain.entity.Comment;
import com.ssafy.happyhouse.domain.entity.Member;
import com.ssafy.happyhouse.domain.entity.RefreshToken;
import com.ssafy.happyhouse.domain.entity.Role;
import com.ssafy.happyhouse.domain.enums.Authority;
import com.ssafy.happyhouse.domain.enums.JwtValidationResults;
import com.ssafy.happyhouse.service.exception.AuthException;
import com.ssafy.happyhouse.service.provider.JwtTokenProvider;
import com.ssafy.happyhouse.service.repository.AuthorityRepository;
import com.ssafy.happyhouse.service.repository.MemberRepository;
import com.ssafy.happyhouse.service.repository.RefreshTokenRepository;

@Service
public class MemberService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Member signup(MemberSignupDto createDto)
            throws IllegalIdentifierException {
        final Member newMember = createDto.toEntity(passwordEncoder);

        // 유저 권한 추가
        final Role userRole = authorityRepository.findByAuthorityIs(Authority.USER).orElseThrow();
        newMember.addAuthority(userRole);

        final Member member = memberRepository.save(newMember);
        logger.debug("Create new member : {}", member);
        return member;
    }

    @Transactional
    public Member updatePassword(String username, String email, String newPassword) {
        final Member member = memberRepository.findMemberByUsernameAndEmail(username, email)
                                              .orElseThrow();
        if (hasText(newPassword)) {
            final String encodedPassword = passwordEncoder.encode(newPassword);
            member.setPassword(encodedPassword);
            member.setPasswordUpdateDate(LocalDateTime.now());
        }
        return memberRepository.save(member);
    }

    public List<Article> getArticles(Long memberId) {
        final Member member = memberRepository.findById(memberId).orElseThrow();
        return member.getArticles();
    }

    public List<Comment> getComments(Long memberId) {
        final Member member = memberRepository.findById(memberId).orElseThrow();
        return member.getComments();
    }

    /**
     * @param username the username identifying the user whose data is required.
     * @throws UsernameNotFoundException 유저이름을 찾을 수 없음
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        return memberRepository.findMemberByUsername(username)
                               .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));
    }

    public Member findById(long memberId) throws NoSuchElementException {
        return memberRepository.findById(memberId).orElseThrow();
    }

    public Member update(long memberId, MemberUpdateInfoDto updateInfoDto) throws NoSuchElementException {
        final Member member = memberRepository.findById(memberId).orElseThrow();
        logger.error(updateInfoDto.toString());
        final Member updatedMember = member.updateInfoFrom(updateInfoDto);
        logger.error(updatedMember.getNickname());
        return memberRepository.save(updatedMember);
    }

    public void delete(long memberId) throws NoSuchElementException {
        final Member member = memberRepository.findById(memberId).orElseThrow();
        memberRepository.delete(member);
    }

    @Autowired
    public MemberService(
            MemberRepository memberRepository,
            AuthorityRepository authorityRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.memberRepository = memberRepository;
        this.authorityRepository = authorityRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }
}
