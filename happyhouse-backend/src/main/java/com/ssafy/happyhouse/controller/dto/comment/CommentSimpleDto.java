package com.ssafy.happyhouse.controller.dto.comment;

import java.time.LocalDateTime;

import com.ssafy.happyhouse.domain.entity.Comment;

import lombok.Builder;
import lombok.Data;

@Data
public class CommentSimpleDto {

    private Long id;
    private Long articleId;
    private String content;
    private String author;
    private LocalDateTime createDate;

    @Builder
    public CommentSimpleDto(Long id, Long articleId, String content, String author, LocalDateTime createDate) {
        this.id = id;
        this.articleId = articleId;
        this.content = content;
        this.author = author;
        this.createDate = createDate;
    }

    public static CommentSimpleDto from(Comment entity) {
        final String author = entity.getAuthor().getNickname();
        return builder()
                .id(entity.getId())
                .articleId(entity.getArticle().getId())
                .content(entity.getContent())
                .author(author)
                .createDate(entity.getCreatedDate())
                .build();
    }
}
