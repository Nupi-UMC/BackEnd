package com.project.nupibe.domain.member.service;

import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.repository.MemberRepository;
import com.project.nupibe.domain.member.dto.request.RequestSignupDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signup(RequestSignupDto signupDto) {
        if (memberRepository.findByEmail(signupDto.email()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        Member member = Member.builder()
                .email(signupDto.email())
                .password(passwordEncoder.encode(signupDto.password())) // 비밀번호 암호화
                .nickname(signupDto.nickname())
                .profile(signupDto.profile())
                .isVerified(false) // 기본값: 이메일 인증 안 됨
                .build();

        memberRepository.save(member);
    }
}