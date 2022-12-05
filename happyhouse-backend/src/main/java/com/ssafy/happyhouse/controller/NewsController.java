package com.ssafy.happyhouse.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.happyhouse.controller.dto.news.NewsDetailDto;
import com.ssafy.happyhouse.domain.entity.News;
import com.ssafy.happyhouse.service.NewsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "주택 뉴스")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/news")
public class NewsController {

    private final static Logger logger = LoggerFactory.getLogger(NewsController.class);
    private final NewsService newsService;

    @Operation(summary = "주택 뉴스 조회")
    @GetMapping
    public ResponseEntity<List<NewsDetailDto>> getNews() {
        try {
            final List<News> newsList = newsService.getNews();
            final List<NewsDetailDto> detailDto = newsList.stream()
                                                          .map(NewsDetailDto::from)
                                                          .collect(Collectors.toList());
            return ResponseEntity.ok(detailDto);
        } catch (IOException e) {
            logger.error(e.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }
}
