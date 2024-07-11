package com.test.blog.service;

import com.test.blog.domain.User;
import com.test.blog.dto.AddUserRequest;
import com.test.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /*
    비밀번호를 데이터베이스에 저장하기 전에 암호화
    WebSecurityConfig에서 비밀번호를 동일한 방식으로 암호화하여 저장된 해시 값과 비교
    * */


    public Long save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
    }

}
