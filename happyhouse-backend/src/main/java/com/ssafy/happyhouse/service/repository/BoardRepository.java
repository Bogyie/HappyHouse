package com.ssafy.happyhouse.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.happyhouse.domain.entity.Article;
import com.ssafy.happyhouse.domain.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}