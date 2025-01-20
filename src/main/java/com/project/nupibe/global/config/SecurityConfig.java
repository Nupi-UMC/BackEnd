package com.project.nupibe.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CORS 정책 설정
        http
                .cors(cors -> cors
                        .configurationSource(CorsConfig.apiConfigurationSource()));

        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Swagger 경로 허용
                        .requestMatchers("/api/**").permitAll() //회원가입 전 기능 테스트를 위해 추가
                        .requestMatchers("/api/auth/**").permitAll() // 인증 관련 API 허용
                        .anyRequest().authenticated() // 나머지 모든 요청은 인증 필요
                )
                .httpBasic(httpBasic -> httpBasic.disable()); // HTTP Basic 인증 비활성화

        return http.build();
    }
}