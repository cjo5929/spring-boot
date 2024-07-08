package com.test.blog.repository;

import com.test.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // email로 사용자 정보를 가져옴
    // 메서드 이름을 분석해 자동 쿼리 생성
    Optional<User> findByEmail(String email);
}
