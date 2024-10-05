package org.example.backend.common;

import org.example.backend.domain.member.repository.MembersRepository;
import org.example.backend.domain.member.service.MembersDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final MembersRepository membersRepository;

    public SecurityConfig(MembersRepository membersRepository) {
        this.membersRepository = membersRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // CSRF 보호를 비활성화
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/api/v1/member/login",
                                "/api/v1/member/signup",
                                "/swagger/**" // 이 경로는 모든 사용자에게 허용
                        ).permitAll()
                        .requestMatchers("/api/v1/member/logout").hasRole("USER") // 로그아웃은 USER 권한이 있어야 접근 가능
                        .anyRequest().authenticated() // 그 외의 모든 요청은 인증이 필요
                )
                .sessionManagement(session -> session
                        .sessionFixation().migrateSession() // 세션 고정 공격 방지를 위한 설정
                        .maximumSessions(-1) // 최대 세션 수를 제한하지 않음
                )
                .formLogin(form -> form.loginPage("/login")) // 커스텀 로그인 페이지 설정
                .logout(logout -> logout
                        .logoutUrl("/api/v1/member/logout") // 로그아웃 URL 설정
                        .deleteCookies("JSESSIONID") // 로그아웃 시 JSESSIONID 쿠키 삭제
                );

        return http.build(); // SecurityFilterChain 반환
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); // AuthenticationProvider 생성
        provider.setUserDetailsService(userDetailsService); // UserDetailsService 설정
        provider.setPasswordEncoder(passwordEncoder); // PasswordEncoder 설정
        return new ProviderManager(Collections.singletonList(provider)); // ProviderManager에 provider 추가하여 반환
    }

    @Bean
    public UserDetailsService userDetailsService() {
        //userDetailService라는 인터페이스의 구현체로 내가 만든 클레스를 쓴다는거다
        // 사용자 정보를 가져오는 CustomUserDetailsService를 생성
        return new MembersDetailService(membersRepository,passwordEncoder()); // 사용자 정의 UserDetailsService 반환
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 인코딩을 위한 BCryptPasswordEncoder 반환
    }
}
