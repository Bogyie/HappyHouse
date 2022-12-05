package com.ssafy.happyhouse.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.happyhouse.domain.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}