package com.project.nupibe.domain.member.service;

import com.project.nupibe.domain.member.dto.request.RequestVerifyDto;
import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.exception.code.VerifyErrorCode;
import com.project.nupibe.domain.member.exception.handler.MemberException;
import com.project.nupibe.domain.member.redis.RedisService;
import com.project.nupibe.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class VerifyService {

    private final RedisService redisService;

    public VerifyService(RedisService redisService) {
        this.redisService = redisService;
    }

    public Long verifyCode(RequestVerifyDto requestDto) {
        // Redis에서 인증 코드 가져오기
        String storedCode = redisService.getVerificationCode(requestDto.email());
        if (storedCode == null) {
            throw new MemberException(VerifyErrorCode.CODE_EXPIRED);
        }
        if (!storedCode.equals(requestDto.verificationCode())) {
            throw new MemberException(VerifyErrorCode.CODE_MISMATCH);
        }

        // Redis에 인증 상태 업데이트
        redisService.setVerifiedStatus(requestDto.email(), true);

        // Redis에서 자동 증가된 verificationId 생성
        return redisService.generateVerificationId();
    }
}