package com.ssafy.happyhouse.controller.dto.article;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ArticleUpdateDto {

    @Schema(description = "글 제목")
    private String title;

    @Schema(description = "글 내용 (JSON)")
    private String content;

    @Schema(description = "비공개 여부")
    private Boolean secret;
}
