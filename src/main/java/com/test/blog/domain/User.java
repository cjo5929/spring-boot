package com.test.blog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
//기본생성자 protected
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User implements UserDetails { // UserDetails를 상속받아 인증 객체로 사용

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "nickname", unique = true)
    private String nickname;


    @Builder
    public User(String email, String password, String auth, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    //  권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }


    // 사용자의 패스워드 반환
    @Override
    public String getPassword() {
        return password;
    }

    // 사용자의 id를 반환(고유한 값)
    @Override
    public String getUsername() {
        return email;
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        // 계정이 사용 가능한지 확인하는 로직

        return true;
    }

    // 사용자 이름 변경
    // update를 도메인에 하는이유
    /*
    * 비즈니스 로직의 응집성
    * 사용자와 관련된 모든 로직을 한 곳에서 관리 가능
    *
    * */
    public User update(String nickname) {
        this.nickname = nickname;

        return this;
    }
}
