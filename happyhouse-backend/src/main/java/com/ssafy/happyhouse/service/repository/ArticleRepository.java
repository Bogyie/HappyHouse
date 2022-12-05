package com.ssafy.happyhouse.service.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.happyhouse.domain.entity.Article;
import com.ssafy.happyhouse.domain.entity.Board;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findArticlesByTitle(String title, Pageable pageable);

    Page<Article> findArticlesByBoard(Board board, Pageable pageable);
}