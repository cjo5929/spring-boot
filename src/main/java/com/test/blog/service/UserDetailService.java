package com.test.blog.service;

import com.test.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    /*
    * 사용자 이름(email)으로 사용자의 정보를 가져오는 메서드
    * UsernameNotFoundException : 사용자가 존재하지 않을 때 던지는 예외
    * Optional을 하면 직접 null을 처리하는 것보다 더 안전함 => NullPointerException 방지
    * null을 반환하는 대신 Optional.empty()를 반환
    * */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }
}
