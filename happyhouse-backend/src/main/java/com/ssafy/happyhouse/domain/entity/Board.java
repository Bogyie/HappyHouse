package com.ssafy.happyhouse.domain.entity;

import static org.springframework.util.StringUtils.hasText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.ssafy.happyhouse.controller.dto.board.BoardUpdateDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "BOARD")
@DynamicInsert
@DynamicUpdate
@Getter @Setter @NoArgsConstructor
public class Board extends BaseTime implements Serializable {

    private static final long serialVersionUID = -3156681655106539317L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", unique = true)
    private String name;

    @OneToMany(mappedBy = "board")
    private List<Article> articles = new ArrayList<>();

    public Board update(BoardUpdateDto updateDto) {

        final String name = updateDto.getName();
        if (hasText(name)) {
            this.name = name;
        }

        return this;
    }

    @Builder
    public Board(String name) {
        this.name = name;
    }
}
