package com.ssafy.happyhouse.controller.dto.news;

import com.ssafy.happyhouse.domain.entity.News;

import lombok.Builder;
import lombok.Data;

@Data
public class NewsDetailDto {
    private String title;
    private String pubDate;
    private String link;
    private String guid;
    private String author;
    private String thumbnail;
    private String description;
    private String content;

    @Builder
    public NewsDetailDto(String title, String pubDate, String link, String guid, String author, String thumbnail,
                         String description, String content) {
        this.title = title;
        this.pubDate = pubDate;
        this.link = link;
        this.guid = guid;
        this.author = author;
        this.thumbnail = thumbnail;
        this.description = description;
        this.content = content;
    }

    public static NewsDetailDto from(News entity) {
        return builder()
                .title(entity.getTitle())
                .pubDate(entity.getPubDate())
                .link(entity.getLink())
                .guid(entity.getGuid())
                .author(entity.getAuthor())
                .thumbnail(entity.getThumbnail())
                .description(entity.getDescription())
                .content(entity.getContent())
                .build();
    }
}
