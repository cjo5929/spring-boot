package com.test.blog.repository;

import com.test.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /*
     * email로 사용자 정보를 가져옴
     * 메서드 이름을 분석해 자동 쿼리 생성
     * Optional을 하면 직접 null을 처리하는 것보다 더 안전함 => NullPointerException 방지
     * null을 반환하는 대신 Optional.empty()를 반환
     * */
    Optional<User> findByEmail(String email);
}
