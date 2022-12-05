package com.ssafy.happyhouse.domain.entity;

import static org.springframework.util.StringUtils.hasText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.ssafy.happyhouse.controller.dto.article.ArticleUpdateDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ARTICLE")
@DynamicInsert  // `insert` 시 `null`인 필드 제외
@DynamicUpdate  // `update` 시 `null`인 필드 제외
@Getter @Setter @NoArgsConstructor
public class Article extends BaseTime implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT", columnDefinition = "TEXT")
    private String content;

    @Column(name = "SECRET")
    private Boolean secret;

    @Column(name = "VIEWS")
    private Integer viewCount;
    @ManyToOne
    @JoinColumn(name = "BOARD")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "AUTHOR")
    private Member author;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public Article updateFrom(ArticleUpdateDto updateDto) {

        final String title = updateDto.getTitle();
        if (hasText(title)) { // check null & empty
            this.title = title;
        }

        final String content = updateDto.getContent();
        if (hasText(content)) {
            this.content = content;
        }

        final Boolean secret = updateDto.getSecret();
        if (secret != null) {
            this.secret = secret;
        }

        return this;
    }

    @Builder
    public Article(String title, String content, Boolean secret, Board board, Member author) {
        this.title = title;
        this.content = content;
        this.secret = secret;
        this.viewCount = 0;
        this.board = board;
        this.author = author;
    }
}
