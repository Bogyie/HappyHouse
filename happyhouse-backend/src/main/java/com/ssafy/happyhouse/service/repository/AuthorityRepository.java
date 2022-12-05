package com.ssafy.happyhouse.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.happyhouse.domain.entity.Role;
import com.ssafy.happyhouse.domain.enums.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByAuthorityIs(Authority authority);
}