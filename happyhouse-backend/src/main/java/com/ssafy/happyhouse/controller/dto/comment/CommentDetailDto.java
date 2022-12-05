package com.ssafy.happyhouse.controller.dto.comment;

import java.time.LocalDateTime;

import com.ssafy.happyhouse.domain.entity.Article;
import com.ssafy.happyhouse.domain.entity.Comment;
import com.ssafy.happyhouse.domain.entity.Member;

import lombok.Builder;
import lombok.Data;

@Data
public class CommentDetailDto {

    private Long id;
    private String content;
    private String author;
    private Long authorId;
    private String article;
    private Long articleId;
    private Boolean secret;
    private LocalDateTime createDate;

    @Builder
    public CommentDetailDto(Long id, String content, String author, Long authorId, String article,
                            Long articleId,
                            Boolean secret, LocalDateTime createDate) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.authorId = authorId;
        this.article = article;
        this.articleId = articleId;
        this.secret = secret;
        this.createDate = createDate;
    }

    public static CommentDetailDto from(Comment entity) {
        final Member author = entity.getAuthor();
        final Article article = entity.getArticle();
        return builder()
                .id(entity.getId())
                .content(entity.getContent())
                .author(author.getNickname())
                .authorId(author.getId())
                .article(article.getTitle())
                .articleId(article.getId())
                .secret(entity.getSecret())
                .createDate(entity.getCreatedDate())
                .build();
    }
}
