package com.ssafy.happyhouse.service;

import java.util.NoSuchElementException;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.ssafy.happyhouse.controller.dto.comment.CommentCreateDto;
import com.ssafy.happyhouse.domain.entity.Article;
import com.ssafy.happyhouse.domain.entity.Comment;
import com.ssafy.happyhouse.domain.entity.Member;
import com.ssafy.happyhouse.service.repository.ArticleRepository;
import com.ssafy.happyhouse.service.repository.CommentRepository;
import com.ssafy.happyhouse.service.repository.MemberRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final Function<Long, Member> getMemberFromId;
    private final Function<Long, Article> getArticleFromId;

    public Comment save(CommentCreateDto createDto)
            throws IllegalArgumentException, NoSuchElementException {
        final Comment comment = createDto.toEntity(getMemberFromId, getArticleFromId);
        return commentRepository.save(comment);
    }

    public Comment findById(Long commentId) throws NoSuchElementException {
        return commentRepository.findById(commentId).orElseThrow();
    }

    public void delete(Long commentId) throws NoSuchElementException {
        final Comment comment = commentRepository.findById(commentId).orElseThrow();
        commentRepository.delete(comment);
    }

    public CommentService(
            CommentRepository commentRepository,
            MemberRepository memberRepository,
            ArticleRepository articleRepository
    ) {
        this.commentRepository = commentRepository;
        getMemberFromId = memberId -> memberRepository.findById(memberId).orElseThrow();
        getArticleFromId = articleId -> articleRepository.findById(articleId).orElseThrow();
    }
}
