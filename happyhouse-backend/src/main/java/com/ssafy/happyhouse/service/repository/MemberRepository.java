package com.ssafy.happyhouse.service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.happyhouse.domain.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByUsername(String username);
    Optional<Member> findMemberByUsernameAndEmail(String username, String email);
    List<Member> findMembersByUsername(String username, Pageable pageable);
    List<Member> findMembersByNickname(String nickname, Pageable pageable);
    List<Member> findMembersByEmail(String email, Pageable pageable);

    boolean existsByUsername(String username);
}