package com.project.nupibe.domain.member.service;

import com.project.nupibe.domain.member.exception.code.LogoutErrorCode;
import com.project.nupibe.domain.member.exception.handler.MemberException;
import com.project.nupibe.domain.member.jwt.JwtTokenProvider;
import com.project.nupibe.domain.member.redis.RedisService;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    public LogoutService(JwtTokenProvider jwtTokenProvider, RedisService redisService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisService = redisService;
    }

    public void logout(String accessToken) {
        // 1. 토큰 유효성 검증
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new MemberException(LogoutErrorCode.INVALID_ACCESS_TOKEN);
        }

        // 2. Access Token을 블랙리스트에 추가
        long remainingExpiration = jwtTokenProvider.getRemainingExpiration(accessToken);
        redisService.addToBlacklist(accessToken, remainingExpiration);

        // 3. Refresh Token 삭제
        String email = jwtTokenProvider.extractEmail(accessToken);
        redisService.deleteRefreshToken(email);
    }
}