package com.ssafy.happyhouse.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.happyhouse.controller.dto.article.ArticleSimpleDto;
import com.ssafy.happyhouse.controller.dto.comment.CommentSimpleDto;
import com.ssafy.happyhouse.controller.dto.member.MemberDetailDto;
import com.ssafy.happyhouse.controller.dto.member.MemberLoginDto;
import com.ssafy.happyhouse.controller.dto.member.MemberPasswordUpdateDto;
import com.ssafy.happyhouse.controller.dto.member.MemberSignupDto;
import com.ssafy.happyhouse.controller.dto.member.MemberUpdateInfoDto;
import com.ssafy.happyhouse.controller.protocol.TokenSpec;
import com.ssafy.happyhouse.domain.entity.Article;
import com.ssafy.happyhouse.domain.entity.Member;
import com.ssafy.happyhouse.service.AuthService;
import com.ssafy.happyhouse.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "멤버", description = "멤버 관련 API")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/member")
public class MemberController {

    private final AuthService authService;
    private final MemberService memberService;
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Operation(summary = "회원가입")
    @PostMapping
    public ResponseEntity<MemberDetailDto> signup(@RequestBody MemberSignupDto signupDto) {
        try {
            final Member entity = memberService.signup(signupDto);
            return ResponseEntity.ok(MemberDetailDto.from(entity));
        } catch (IllegalIdentifierException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 상세 정보
     */
    @Operation(summary = "회원 상세 정보")
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDetailDto> detail(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
            @PathVariable("memberId") Long memberId
    ) {
        try {

            // 인증 실패
            if (!authService.verifySelfOrAdmin(accessToken, memberId)) {
                return ResponseEntity.badRequest().build();
            }

            final Member entity = memberService.findById(memberId);
            return ResponseEntity.ok(MemberDetailDto.from(entity));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 유저 정보 업데이트
     */
    @Operation(summary = "회원 정보 업데이트")
    @PutMapping("/{memberId}")
    public ResponseEntity<MemberDetailDto> update(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
            @PathVariable("memberId") Long memberId,
            @RequestBody MemberUpdateInfoDto updateDto
    ) {
        try {

            // 인증 실패
            if (!authService.verifySelfOrAdmin(accessToken, memberId)) {
                return ResponseEntity.badRequest().build();
            }

            final Member entity = memberService.update(memberId, updateDto);
            return ResponseEntity.ok(MemberDetailDto.from(entity));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<TokenSpec> login(@RequestBody MemberLoginDto loginDto) {
        try {
            final TokenSpec tokenSpec = authService.login(loginDto);
            return ResponseEntity.ok(tokenSpec);
        } catch (IllegalIdentifierException e) {
            return ResponseEntity.badRequest().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 회원 탈퇴
     */
    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> delete(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
            @PathVariable("memberId") Long memberId
    ) {
        try {

            // 인증 실패
            if (!authService.verifySelfOrAdmin(accessToken, memberId)) {
                return ResponseEntity.badRequest().build();
            }

            memberService.delete(memberId);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "비밀번호 변경")
    @PutMapping("/password")
    public ResponseEntity<MemberDetailDto> updatePassword(
            @RequestBody MemberPasswordUpdateDto passwordUpdateDto
    ) {
        try {
            final Member entity = memberService.updatePassword(
                    passwordUpdateDto.getUsername(),
                    passwordUpdateDto.getEmail(),
                    passwordUpdateDto.getPassword());

            return ResponseEntity.ok(MemberDetailDto.from(entity));
        } catch (IllegalArgumentException e) {
            // 비밀번호가 빈칸이면 안됨
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{memberId}/articles")
    public ResponseEntity<List<ArticleSimpleDto>> getArticles(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
            @PathVariable("memberId") Long memberId
    ) {
        try {

            // 인증 실패
            if (!authService.verifySelfOrAdmin(accessToken, memberId)) {
                return ResponseEntity.badRequest().build();
            }

            final List<ArticleSimpleDto> articles = memberService.getArticles(memberId)
                                                                 .stream()
                                                                 .map(ArticleSimpleDto::from)
                                                                 .collect(Collectors.toList());

            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{memberId}/comments")
    public ResponseEntity<List<CommentSimpleDto>> getComments(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
            @PathVariable("memberId") Long memberId
    ) {
        try {

            // 인증 실패
            if (!authService.verifySelfOrAdmin(accessToken, memberId)) {
                return ResponseEntity.badRequest().build();
            }
            final List<CommentSimpleDto> comments = memberService.getComments(memberId)
                                                                 .stream()
                                                                 .map(CommentSimpleDto::from)
                                                                 .collect(Collectors.toList());
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Autowired
    public MemberController(
            AuthService authService,
            MemberService memberService
    ) {
        this.authService = authService;
        this.memberService = memberService;
    }
}
