package com.project.nupibe.domain.member.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveVerificationCode(String email, String code, long ttl) {
        redisTemplate.opsForValue().set("VC:" + email, code, ttl, TimeUnit.SECONDS);
    }

    public String getVerificationCode(String email) {
        return redisTemplate.opsForValue().get("VC:" + email);
    }

    public void setVerifiedStatus(String email, boolean status) {
        redisTemplate.opsForValue().set("VS:" + email, String.valueOf(status));
    }

    public boolean isVerified(String email) {
        String status = redisTemplate.opsForValue().get("VS:" + email);
        return status != null && Boolean.parseBoolean(status);
    }

    public Long generateVerificationId() {
        return redisTemplate.opsForValue().increment("GLOBAL_VERIFICATION_ID");
    }

    // Refresh Token 저장
    public void saveRefreshToken(String email, String refreshToken, long ttl) {
        redisTemplate.opsForValue().set("RT:" + email, refreshToken, ttl, TimeUnit.SECONDS);
    }

    // Refresh Token 가져오기
    public String getRefreshToken(String email) {
        return redisTemplate.opsForValue().get("RT:" + email);
    }

    // Refresh Token 삭제
    public void deleteRefreshToken(String email) {
        redisTemplate.delete("RT:" + email);
    }

    // Access Token 블랙리스트 추가
    public void addToBlacklist(String token, long ttl) {
        redisTemplate.opsForValue().set("BL:" + token, "true", ttl, TimeUnit.SECONDS);
    }

    // Access Token 블랙리스트 확인
    public boolean isBlacklisted(String token) {
        return redisTemplate.opsForValue().get("BL:" + token) != null;
    }
}