package com.ssafy.happyhouse.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import javax.persistence.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.happyhouse.controller.dto.article.ArticleCreateDto;
import com.ssafy.happyhouse.controller.dto.article.ArticleUpdateDto;
import com.ssafy.happyhouse.domain.entity.Article;
import com.ssafy.happyhouse.domain.entity.Board;
import com.ssafy.happyhouse.domain.entity.Comment;
import com.ssafy.happyhouse.domain.entity.Member;
import com.ssafy.happyhouse.service.repository.ArticleRepository;
import com.ssafy.happyhouse.service.repository.BoardRepository;
import com.ssafy.happyhouse.service.repository.MemberRepository;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final Function<Long, Member> getMemberFromId;
    private final Function<Long, Board> getBoardFromId;

    public Page<Article> findByBoard(long boardId, Pageable pageable) {
        final Board board = getBoardFromId.apply(boardId);
        return articleRepository.findArticlesByBoard(board, pageable);
    }

    public Page<Article> findByTitle(String title, Pageable pageable) {
        return articleRepository.findArticlesByTitle(title, pageable);
    }

    /**
     *
     * @param createDto {@link ArticleCreateDto}
     * @return {@link Article}
     * @throws NoSuchElementException memberId, boardId 로부터 해당하는 {@link Entity}를 찾지 못한 경우 발생
     */
    @Transactional
    public Article save(ArticleCreateDto createDto)
            throws NoSuchElementException, IllegalArgumentException  {
        final Article article = createDto.toEntity(getMemberFromId, getBoardFromId);
        return articleRepository.save(article);
    }

    public Article find(Long articleId) throws NoSuchElementException {
        return articleRepository.findById(articleId).orElseThrow();
    }

    public Article updateFrom(Long articleId, ArticleUpdateDto updateDto)
        throws NoSuchElementException {
        final Article article = articleRepository.findById(articleId).orElseThrow();
        final Article updatedArticle = article.updateFrom(updateDto);
        return articleRepository.save(updatedArticle);
    }

    public void delete(Long articleId) throws NoSuchElementException {
        final Article article = articleRepository.findById(articleId).orElseThrow();
        articleRepository.delete(article);
    }

    public List<Comment> getComments(Long articleId) throws NoSuchElementException {
        final Article article = articleRepository.findById(articleId).orElseThrow();
        return article.getComments();
    }

    public void increaseViewCount(Long articleId) {
        final Article article = articleRepository.findById(articleId).orElseThrow();
        final int viewCount = article.getViewCount();
        article.setViewCount(viewCount + 1);
        articleRepository.save(article);
    }

    @Autowired
    public ArticleService(
            ArticleRepository articleRepository,
            BoardRepository boardRepository,
            MemberRepository memberRepository
    ) {
        this.articleRepository = articleRepository;
        getMemberFromId = memberId -> memberRepository.findById(memberId).orElseThrow();
        getBoardFromId = boardId -> boardRepository.findById(boardId).orElseThrow();
    }
}
