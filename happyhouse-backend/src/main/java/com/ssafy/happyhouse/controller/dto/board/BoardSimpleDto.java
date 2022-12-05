package com.ssafy.happyhouse.controller.dto.board;

import com.ssafy.happyhouse.domain.entity.Board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardSimpleDto {

    private Long id;
    private String name;

    @Builder
    public BoardSimpleDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static BoardSimpleDto from(Board entity) {
        return builder().id(entity.getId())
                        .name(entity.getName())
                        .build();
    }
}
