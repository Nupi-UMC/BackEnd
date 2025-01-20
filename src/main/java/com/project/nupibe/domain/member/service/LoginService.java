package com.project.nupibe.domain.member.service;

import com.project.nupibe.domain.member.dto.request.RequestLoginDto;
import com.project.nupibe.domain.member.dto.response.ResponseLoginDto;
import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.exception.code.LoginErrorCode;
import com.project.nupibe.domain.member.exception.handler.MemberException;
import com.project.nupibe.domain.member.jwt.JwtTokenProvider;
import com.project.nupibe.domain.member.redis.RedisService;
import com.project.nupibe.domain.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    public LoginService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, RedisService redisService) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisService = redisService;
    }

    public ResponseLoginDto login(RequestLoginDto loginRequestDto) {
        // 1. 이메일로 사용자 조회
        Member member = memberRepository.findByEmail(loginRequestDto.email())
                .orElseThrow(() -> new MemberException(LoginErrorCode.EMAIL_OR_PASSWORD_INVALID));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(loginRequestDto.password(), member.getPassword())) {
            throw new MemberException(LoginErrorCode.EMAIL_OR_PASSWORD_INVALID);
        }

        // 3. 기존 Refresh Token 삭제 (블랙리스트에 추가)
        String existingRefreshToken = redisService.getRefreshToken(member.getEmail());
        if (existingRefreshToken != null) {
            redisService.addToBlacklist(existingRefreshToken, jwtTokenProvider.getRefreshTokenValidity());
            redisService.deleteRefreshToken(member.getEmail());
        }

        // 4. Access Token 및 Refresh Token 생성
        String accessToken = jwtTokenProvider.generateAccessToken(member.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getEmail());

        // 5. Redis에 Refresh Token 저장
        redisService.saveRefreshToken(member.getEmail(), refreshToken, jwtTokenProvider.getRefreshTokenValidity());

        // 6. 로그인 성공 응답 생성
        return new ResponseLoginDto(
                accessToken,
                refreshToken,
                jwtTokenProvider.getAccessTokenValidity()
        );
    }
}