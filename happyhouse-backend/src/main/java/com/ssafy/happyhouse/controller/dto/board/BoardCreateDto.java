package com.ssafy.happyhouse.controller.dto.board;

import com.ssafy.happyhouse.domain.entity.Board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BoardCreateDto {

    @Schema(description = "게시판 이름")
    private String name;
    public Board toEntity() throws IllegalArgumentException {
        return Board.builder()
                    .name(name)
                    .build();
    }
}
