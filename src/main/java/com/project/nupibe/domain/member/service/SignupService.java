package com.project.nupibe.domain.member.service;

import com.project.nupibe.domain.member.dto.request.RequestSignupDto;
import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.exception.code.VerifyErrorCode;
import com.project.nupibe.domain.member.exception.code.SignupErrorCode;
import com.project.nupibe.domain.member.exception.handler.MemberException;
import com.project.nupibe.domain.member.repository.MemberRepository;
import com.project.nupibe.domain.member.redis.RedisService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignupService {

    private final MemberRepository memberRepository;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;

    public SignupService(MemberRepository memberRepository, RedisService redisService, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.redisService = redisService;
        this.passwordEncoder = passwordEncoder;
    }

    public Long signup(RequestSignupDto signupDto) {
        // 이메일 중복 체크
        if (memberRepository.findByEmail(signupDto.email()).isPresent()) {
            throw new MemberException(SignupErrorCode.EMAIL_ALREADY_REGISTERED);
        }

        // Redis에서 인증 상태 확인
        if (!redisService.isVerified(signupDto.email())) {
            throw new MemberException(SignupErrorCode.EMAIL_NOT_VERIFIED);
        }

        // 사용자 생성 및 저장
        Member member = Member.builder()
                .email(signupDto.email())
                .password(passwordEncoder.encode(signupDto.password()))
                .nickname(signupDto.nickname())
                .profile(signupDto.profile())
                .isVerified(true) // 인증 상태 저장
                .build();

        return memberRepository.save(member).getId();
    }
}