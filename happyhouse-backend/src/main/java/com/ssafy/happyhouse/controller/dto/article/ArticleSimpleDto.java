package com.ssafy.happyhouse.controller.dto.article;

import java.time.LocalDateTime;

import com.ssafy.happyhouse.domain.entity.Article;

import lombok.Builder;
import lombok.Data;

@Data
public class ArticleSimpleDto {
    private Long id;
    private String title;
    private String author;
    private int views;
    private LocalDateTime createdDate;

    @Builder
    public ArticleSimpleDto(Long id, String title, String author, int views, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.views = views;
        this.createdDate = createdDate;
    }

    public static ArticleSimpleDto from(Article entity) {
        final String author = entity.getAuthor().getNickname();
        return builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .author(author)
                .views(entity.getViewCount())
                .createdDate(entity.getCreatedDate())
                .build();
    }
}
