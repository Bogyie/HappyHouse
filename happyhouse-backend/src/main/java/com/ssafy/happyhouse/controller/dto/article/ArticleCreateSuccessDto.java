package com.ssafy.happyhouse.controller.dto.article;

import com.ssafy.happyhouse.domain.entity.Article;

import lombok.Builder;
import lombok.Data;

@Data
public class ArticleCreateSuccessDto {
    private Long articleId;

    @Builder
    public ArticleCreateSuccessDto(Long articleId) {
        this.articleId = articleId;
    }

    public static ArticleCreateSuccessDto from(Article entity) {
        return builder()
                .articleId(entity.getId())
                .build();
    }
}
