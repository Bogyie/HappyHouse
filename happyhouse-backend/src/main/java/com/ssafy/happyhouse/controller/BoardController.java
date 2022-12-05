package com.ssafy.happyhouse.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.happyhouse.controller.dto.article.ArticleSimpleDto;
import com.ssafy.happyhouse.controller.dto.board.BoardCreateDto;
import com.ssafy.happyhouse.controller.dto.board.BoardDetailDto;
import com.ssafy.happyhouse.controller.dto.board.BoardSimpleDto;
import com.ssafy.happyhouse.controller.dto.board.BoardUpdateDto;
import com.ssafy.happyhouse.domain.entity.Article;
import com.ssafy.happyhouse.domain.entity.Board;
import com.ssafy.happyhouse.service.ArticleService;
import com.ssafy.happyhouse.service.BoardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "게시판", description = "게시판 관련 API")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/board")
public class BoardController {

    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
    private final BoardService boardService;
    private final ArticleService articleService;

    @Operation(summary = "게시판 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = BoardSimpleDto.class)))
            }),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<List<BoardSimpleDto>> list(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        try {
            final Page<Board> boardPage = boardService.getBoards(pageable);
            final List<BoardSimpleDto> boards = boardPage.stream()
                                                         .map(BoardSimpleDto::from)
                                                         .collect(Collectors.toList());
            logger.info("게시판 목록 조회 성공");
            return ResponseEntity.ok(boards);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "게시판 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시판 생성 성공", content = {
                    @Content(schema = @Schema(implementation = BoardSimpleDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Request body 타입 또는 값 검증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<BoardSimpleDto> newBoard(@RequestBody BoardCreateDto boardCreateDto) {
        logger.info("게시판 생성 요청 수신 : name={}", boardCreateDto.getName());
        try {
            final Board entity = boardService.createBoard(boardCreateDto);
            return ResponseEntity.ok(BoardSimpleDto.from(entity));
        } catch (IllegalArgumentException e) {
            logger.debug(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "게시판 상세정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시판 상세정보 조회 성공", content = {
                    @Content(schema = @Schema(implementation = BoardDetailDto.class))
            }),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDetailDto> detail(@PathVariable("boardId") Long boardId) {
        logger.info("게시판 상세정보 조회 요청 수신 : boardId={}", boardId);
        try {
            final Board entity = boardService.getBoard(boardId);
            return ResponseEntity.ok(BoardDetailDto.from(entity));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "게시판 설정 업데이트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시판 상세정보 업데이트 성공", content = {
                    @Content(schema = @Schema(implementation = BoardDetailDto.class))
            }),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PatchMapping("/{boardId}")
    public ResponseEntity<BoardDetailDto> update(
            @PathVariable("boardId") Long boardId,
            @RequestBody BoardUpdateDto updateDto
    ) {
        logger.info("게시판 업데이트 요청 수신 : boardId={}", boardId);
        try {
            final Board board = boardService.updateBoard(boardId, updateDto);
            return ResponseEntity.ok(BoardDetailDto.from(board));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "게시판 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시판 삭제 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> delete(@PathVariable("boardId") Long boardId) {
        logger.info("게시판 삭제 요청 수신 : boardId={}", boardId);
        try {
            boardService.deleteBoard(boardId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{boardId}/articles")
    public ResponseEntity<List<ArticleSimpleDto>> getArticles(
            @PathVariable("boardId") Long boardId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        try {
            final List<ArticleSimpleDto> articles = articleService.findByBoard(boardId, pageable)
                                                                  .stream()
                                                                  .map(ArticleSimpleDto::from)
                                                                  .collect(Collectors.toList());
            return ResponseEntity.ok(articles);
        } catch (NoSuchElementException e) {
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Autowired
    public BoardController(
            BoardService boardService,
            ArticleService articleService
    ) {
        this.boardService = boardService;
        this.articleService = articleService;
    }
}
