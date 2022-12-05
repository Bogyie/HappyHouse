package com.ssafy.happyhouse.controller.dto.board;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BoardUpdateDto {

    @Schema(description = "게시판 이름")
    private String name;
}
