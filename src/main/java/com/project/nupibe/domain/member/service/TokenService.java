package com.project.nupibe.domain.member.service;

import com.project.nupibe.domain.member.dto.response.ResponseTokenDto;
import com.project.nupibe.domain.member.exception.code.TokenErrorCode;
import com.project.nupibe.domain.member.exception.handler.MemberException;
import com.project.nupibe.domain.member.jwt.JwtTokenProvider;
import com.project.nupibe.domain.member.redis.RedisService;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    public TokenService(JwtTokenProvider jwtTokenProvider, RedisService redisService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisService = redisService;
    }

    public ResponseTokenDto refreshToken(String refreshToken) {
        // Refresh Token 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new MemberException(TokenErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 이메일 및 멤버 아이디 추출
        String email = jwtTokenProvider.extractEmail(refreshToken);
        Long memberId = jwtTokenProvider.extractMemberId(refreshToken);

        // Redis에 저장된 Refresh Token과 일치 여부 확인
        String storedRefreshToken = redisService.getRefreshToken(email);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new MemberException(TokenErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 새로운 Access Token 및 Refresh Token 생성
        String newAccessToken = jwtTokenProvider.generateAccessToken(memberId, email);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(email);

        // Redis에 새 Refresh Token 저장 (기존 토큰 대체)
        redisService.saveRefreshToken(email, newRefreshToken, jwtTokenProvider.getRefreshTokenValidity());

        return new ResponseTokenDto(
                newAccessToken,
                newRefreshToken,
                jwtTokenProvider.getAccessTokenValidity()
        );
    }
}