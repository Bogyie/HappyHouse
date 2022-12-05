package com.ssafy.happyhouse.controller.dto.board;

import java.time.LocalDateTime;

import com.ssafy.happyhouse.domain.entity.Board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardDetailDto {

    private Long id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public BoardDetailDto(Long id, String name, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static BoardDetailDto from(Board entity) {
        return builder().id(entity.getId())
                        .name(entity.getName())
                        .createdDate(entity.getCreatedDate())
                        .modifiedDate(entity.getModifiedDate())
                        .build();
    }
}
