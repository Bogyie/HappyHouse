package com.ssafy.happyhouse.controller.dto.comment;

import java.util.function.Function;

import com.ssafy.happyhouse.domain.entity.Article;
import com.ssafy.happyhouse.domain.entity.Comment;
import com.ssafy.happyhouse.domain.entity.Member;

import lombok.Data;

@Data
public class CommentCreateDto {
    private String content;
    private Long authorId;
    private Long articleId;
    private Boolean secret;

    public Comment toEntity(
            Function<Long, Member> getMemberFromId,
            Function<Long, Article> getArticleFromId
    ) {
        final Member author = getMemberFromId.apply(authorId);
        final Article article = getArticleFromId.apply(articleId);
        return Comment.builder()
                      .content(content)
                      .author(author)
                      .article(article)
                      .secret(secret)
                      .build();
    }
}
