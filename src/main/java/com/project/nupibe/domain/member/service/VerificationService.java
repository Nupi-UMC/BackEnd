package com.project.nupibe.domain.member.service;

import com.project.nupibe.domain.member.redis.RedisService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class VerificationService {

    private final RedisService redisService;

    public VerificationService(RedisService redisService) {
        this.redisService = redisService;
    }

    public String requestVerification(String email) {
        // 인증 코드 생성
        String verificationCode = generateVerificationCode();
        long ttl = 300; // 5분

        // Redis에 저장
        redisService.saveVerificationCode(email, verificationCode, ttl);

        // 인증 코드 만료 시간 반환
        return LocalDateTime.now().plusSeconds(ttl).toString();
    }

    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999)); // 6자리 숫자
    }
}