package com.ssafy.happyhouse.controller.dto.article;

import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssafy.happyhouse.domain.entity.Article;
import com.ssafy.happyhouse.domain.entity.Board;
import com.ssafy.happyhouse.domain.entity.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ArticleCreateDto {

    @Schema(description = "글 제목")
    private String title;

    @Schema(description = "글 내용 (JSON)")
    private String content;

    @Schema(description = "작성자 ID")
    private long authorId;

    @Schema(description = "게시판 ID")
    private long boardId;

    @Schema(description = "비공개 여부")
    private boolean secret;

    public Article toEntity(
            Function<Long, Member> getMemberFromId,
            Function<Long, Board> getBoardFromId
    ) {
        final Member author = getMemberFromId.apply(authorId);
        final Board board = getBoardFromId.apply(boardId);
        return Article.builder()
                      .title(title)
                      .content(content)
                      .author(author)
                      .board(board)
                      .secret(secret)
                      .build();
    }
}
