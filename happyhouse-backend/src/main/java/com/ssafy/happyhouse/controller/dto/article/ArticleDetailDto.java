package com.ssafy.happyhouse.controller.dto.article;

import java.time.LocalDateTime;

import com.ssafy.happyhouse.domain.entity.Article;
import com.ssafy.happyhouse.domain.entity.Board;

import lombok.Builder;
import lombok.Data;

@Data
public class ArticleDetailDto {

    private Long articleId;
    private String title;
    private String author;
    private String content;
    private String board;
    private Long boardId;
    private int views;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public ArticleDetailDto(Long articleId, String title, String author, String content, String board, Long boardId,int views, LocalDateTime createdDate,
                            LocalDateTime modifiedDate) {
        this.articleId = articleId;
        this.title = title;
        this.author = author;
        this.content = content;
        this.board = board;
        this.boardId = boardId;
        this.views = views;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static ArticleDetailDto from(Article entity) {
        final String author = entity.getAuthor().getNickname();
        final Board board = entity.getBoard();
        return builder()
                .articleId(entity.getId())
                .title(entity.getTitle())
                .author(author)
                .content(entity.getContent())
                .board(board.getName())
                .boardId(board.getId())
                .views(entity.getViewCount())
                .createdDate(entity.getCreatedDate())
                .modifiedDate(entity.getModifiedDate())
                .build();
    }
}
