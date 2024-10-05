package org.example.backend.common;

import org.example.backend.domain.member.repository.MembersRepository;
import org.example.backend.domain.member.service.MembersDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final MembersRepository membersRepository;

    public SecurityConfig(MembersRepository membersRepository) {
        this.membersRepository = membersRepository;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**", "/swagger-ui.html", "/swagger-ui/index.html");
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/api/v1/member/login",
                                "/api/v1/member/signup",
                                "/swagger-ui/**",         // Swagger UI 경로 허용
                                "/v3/api-docs/**",        // OpenAPI 문서 경로 허용
                                "/swagger-resources/**",  // Swagger 리소스 허용
                                "/webjars/**",            // Swagger에서 사용하는 Webjars 허용
                                "/swagger-ui.html",        // Swagger UI HTML 페이지 허용
                                "/swagger-ui/index.html",   // Swagger UI index 페이지 허용
                                "/api-docs/swagger-config",
                                "/api-docs"
                        ).permitAll()  // 이 경로들은 인증 없이 접근 가능
                        .anyRequest().authenticated()  // 그 외의 모든 요청은 인증이 필요
                )
                .sessionManagement(session -> session
                        .sessionFixation().migrateSession() // 세션 고정 방지 설정
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .maximumSessions(-1) // 동시 세션 제한 제거
                )
                .formLogin(form -> form
                        .loginPage("/login")  // 로그인 페이지
                        .permitAll()  // 모든 사용자에게 로그인 페이지 접근 허용
                )
                .logout(logout -> logout
                        .logoutUrl("/api/v1/member/logout")  // 로그아웃 URL 설정
//                        .logoutSuccessHandler(logoutSuccessHandler())
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")  // 로그아웃 시 JSESSIONID 쿠키 삭제
                ).authenticationProvider(authenticationProvider());

        return http.build();  // SecurityFilterChain 반환
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(
                "http://everycare.site",
                "http://www.everycare.site",
                "https://everycare.site",
                "https://www.everycare.site",
                "https://everycare.site:8080",
                "https://everycare.site:5173",
                "https://everycare.site:5000",
                "http://localhost",
                "http://localhost:8080",
                "http://localhost:5173",
                "http://localhost:5000",
                "http://127.0.0.1:5000",
                "http://flask-server:5000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
