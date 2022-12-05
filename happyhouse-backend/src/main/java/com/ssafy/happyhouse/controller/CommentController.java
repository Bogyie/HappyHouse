package com.ssafy.happyhouse.controller;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.happyhouse.controller.dto.comment.CommentCreateDto;
import com.ssafy.happyhouse.controller.dto.comment.CommentDetailDto;
import com.ssafy.happyhouse.controller.dto.comment.CommentSimpleDto;
import com.ssafy.happyhouse.domain.entity.Comment;
import com.ssafy.happyhouse.service.CommentService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/comment")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentSimpleDto> create(@RequestBody CommentCreateDto createDto) {
        try {
            logger.error(createDto.toString());
            final Comment entity = commentService.save(createDto);
            return ResponseEntity.ok(CommentSimpleDto.from(entity));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDetailDto> find(@PathVariable(name = "commentId") Long commentId) {
        try {
            final Comment entity = commentService.findById(commentId);
            return ResponseEntity.ok(CommentDetailDto.from(entity));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable(name = "commentId") Long commentId) {
        try {
            commentService.delete(commentId);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Autowired
    public CommentController(
            CommentService commentService
    ) {
        this.commentService = commentService;
    }
}
