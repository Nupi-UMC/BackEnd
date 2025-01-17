package com.project.nupibe.domain.redis;

import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

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
}
