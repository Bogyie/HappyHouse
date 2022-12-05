package com.ssafy.happyhouse.service;

import java.util.NoSuchElementException;

import javax.persistence.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.happyhouse.controller.dto.board.BoardCreateDto;
import com.ssafy.happyhouse.controller.dto.board.BoardUpdateDto;
import com.ssafy.happyhouse.domain.entity.Board;
import com.ssafy.happyhouse.service.repository.BoardRepository;

@Service
public class BoardService {

    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);
    private final BoardRepository boardRepository;

    /**
     * {@link Board} 목록을 페이징 처리하여 반환
     * @param pageable {@link Pageable} 페이지 정보를 담은 객체
     * @return {@link Page}{@code <Board>}{@link Entity} 반환
     */
    public Page<Board> getBoards(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    /**
     * 새로운 {@link Board}를 생성
     * @param dto {@link BoardCreateDto}
     * @return 생성된 {@link Board} {@link Entity} 반환
     * @throws IllegalArgumentException {@link BoardRepository#save} 과정에서 예외 발생
     * @throws OptimisticLockingFailureException DataBase Lock 으로 예외 발생
     */
    public Board createBoard(BoardCreateDto dto)
            throws IllegalArgumentException, OptimisticLockingFailureException {
        final Board newBoard = dto.toEntity();
        logger.info("Create new board : {}", newBoard);
        return boardRepository.save(newBoard);
    }

    /**
     * 게시판 엔티티 반환
     * @param boardId {@link Board#id}
     * @return {@link Board} {@link Entity} 반환
     * @throws NoSuchElementException {@code boardId}에 해당하는 {@link Board} {@link Entity}를 찾을 수 없는 경우 예외 발생
     */
    public Board getBoard(Long boardId) throws NoSuchElementException {
        logger.info("게시판 엔티티 조회 : boardId = {}", boardId);
        return boardRepository.findById(boardId).orElseThrow();
    }

    /**
     * 게시판 설정 업데이트
     * @param boardId 업데이트할 {@link Board#id}
     * @param dto {@link BoardUpdateDto} 업데이트할 정보를 담은 DTO 객체
     * @return 업데이트 된 {@link Board} {@link Entity} 반환
     * @throws NoSuchElementException {@code boardId}에 해당하는 {@link Board} {@link Entity}를 찾을 수 없는 경우 예외 발생
     */
    @Transactional
    public Board updateBoard(Long boardId, BoardUpdateDto dto) throws NoSuchElementException {
        final Board board = boardRepository.findById(boardId).orElseThrow();
        final Board updatedBoard = board.update(dto);
        return boardRepository.save(updatedBoard);
    }

    /**
     * 게시판 삭제
     * @param boardId 삭제할 {@link Board#id}
     * @throws NoSuchElementException {@code boardId}에 해당하는 {@link Board} {@link Entity}를 찾을 수 없는 경우 예외 발생
     * @throws IllegalArgumentException {@code boardId}에 해당하는 {@link Board} {@link Entity}를 찾을 수 없는 경우 예외 발생
     * @throws OptimisticLockingFailureException DataBase Lock 으로 예외 발생
     */
    public void deleteBoard(Long boardId)
            throws NoSuchElementException, IllegalArgumentException, OptimisticLockingFailureException {
        logger.info("Delete board : {}", boardId);
        final Board board = boardRepository.findById(boardId).orElseThrow();
        boardRepository.delete(board);
    }

    public BoardService(
            BoardRepository boardRepository
    ) {
        this.boardRepository = boardRepository;
    }
}
