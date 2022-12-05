package com.ssafy.happyhouse.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.happyhouse.controller.dto.article.ArticleCreateSuccessDto;
import com.ssafy.happyhouse.controller.dto.comment.CommentSimpleDto;
import com.ssafy.happyhouse.controller.dto.article.ArticleCreateDto;
import com.ssafy.happyhouse.controller.dto.article.ArticleDetailDto;
import com.ssafy.happyhouse.controller.dto.article.ArticleSimpleDto;
import com.ssafy.happyhouse.controller.dto.article.ArticleUpdateDto;
import com.ssafy.happyhouse.domain.entity.Article;
import com.ssafy.happyhouse.domain.entity.Comment;
import com.ssafy.happyhouse.service.ArticleService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "게시글", description = "게시글 관련 API")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/article")
public class ArticleController {

    private final static Logger logger = LoggerFactory.getLogger(ArticleController.class);
    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<List<ArticleSimpleDto>> search(
            @RequestParam Object articleSearchDto, // FIXME
            @PageableDefault Pageable pageable
            ) {
        // TODO
        throw new NotImplementedException();
    }

    @PostMapping
    public ResponseEntity<ArticleCreateSuccessDto> create(
            @RequestBody ArticleCreateDto createDto
    ) {
        try {
            final Article entity = articleService.save(createDto);
            return ResponseEntity.ok(ArticleCreateSuccessDto.from(entity));
        } catch (NoSuchElementException e) {
            // memberId, boardId 로부터 해당하는 Entity를 찾지 못한 경우 발생
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleDetailDto> detail(
            @PathVariable(name = "articleId") Long articleId
    ) {
        try {
            final Article entity = articleService.find(articleId);
            articleService.increaseViewCount(articleId);
            return ResponseEntity.ok(ArticleDetailDto.from(entity));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<ArticleDetailDto> update(
            @PathVariable(name = "articleId") Long articleId,
            @RequestBody ArticleUpdateDto updateDto
    ) {
        try {
            final Article entity = articleService.updateFrom(articleId, updateDto);
            return ResponseEntity.ok(ArticleDetailDto.from(entity));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> delete(
            @PathVariable(name = "articleId") Long articleId
    ) {
        try {
            articleService.delete(articleId);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{articleId}/comments")
    public ResponseEntity<List<CommentSimpleDto>> comments(
            @PathVariable(name = "articleId") Long articleId
    ) {
        try {
            final List<Comment> comments = articleService.getComments(articleId);
            final List<CommentSimpleDto> commentSimpleDtos = comments.stream()
                                                              .map(CommentSimpleDto::from)
                                                              .collect(Collectors.toList());
            return ResponseEntity.ok(commentSimpleDtos);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }
}
