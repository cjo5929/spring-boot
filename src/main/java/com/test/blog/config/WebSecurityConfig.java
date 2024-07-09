package com.test.blog.config;

import com.test.blog.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailService userService;


    // 스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers("/static/**");
    }

    /*
    Spring Security 버전 (5.7 이상)이 오르면서 책 내용 코드는 더 이상 사용하지 않음
    * https://github.com/shinsunyoung/springboot-developer/issues/5
    * */

    // securityFilterChain 메서드는 특정 HTTP 요청에 대한 웹 기반 보안을 구성
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/login", "/signup", "/user").permitAll() // 이 경로 모든 사용자 접근 허용
                        .anyRequest().authenticated() // 나머지는 인증된 사용자만
                )
                .formLogin((form) -> form
                        .loginPage("/login") // 로그인 페이지 경로
                        .defaultSuccessUrl("/articles") // 로그인 성공 시 이동할 경로
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout") // 로그아웃 경로
                        .logoutSuccessUrl("/login") //로그아웃 성공 시 이동할 경로
                        .invalidateHttpSession(true) // 로그아웃 후 세션 무효화
                        .permitAll()
                )
                .csrf((csrf) -> csrf.disable()); //CSRF 비활성화 => 보안상 실제에는 활성화

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userService); //데이터베이스에서 사용자 정보를 가져오기
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder()); //  비밀번호를 암호화하고 비교하는데 사용

        return daoAuthenticationProvider;
    }

    /*
     * 5.7 이상에서 AuthenticationManagerBuilder를 직접 구성하는 대신 AuthenticationConfiguration을
     * 사용하여 AuthenticationManager를 구성하는것이 권장됨
     * AuthenticationManager은 현재 코드에서 사용하지 않음
     * (스프링시큐리티에서는 명시적으로 선언 안 해도 내부적으로 자동으로 구성하고 관리함)
     * => AuthenticationManager 빈을 주입받아서 수동으로 로그인 로직을 직접 커스텀마이징할 때 사용
     * */
    // 인증 관리자 설정
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    // 패스워드 인코더 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
