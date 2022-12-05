package com.ssafy.happyhouse.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "COMMENT")
@DynamicUpdate
@DynamicInsert
@Getter @Setter @NoArgsConstructor
public class Comment extends BaseTime implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CONTENT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "AUTHOR")
    private Member author;

    @ManyToOne
    @JoinColumn(name = "ARTICLE")
    private Article article;

    @Column(name = "SECRET")
    private Boolean secret;

    @Builder
    public Comment(String content, Member author, Article article, Boolean secret) {
        this.content = content;
        this.author = author;
        this.article = article;
        this.secret = secret;
    }
}
