package com.project.nupibe.global.config;

import com.project.nupibe.domain.member.jwt.JwtTokenProvider;
import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public Long getMemberIdFromToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("VALID400_1: Authorization 헤더가 없거나 올바르지 않습니다.");
        }

        // "Bearer " 제거 후 토큰 추출
        String token = authorizationHeader.substring(7);

        // 토큰 유효성 검사
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("VALID401_1: 유효하지 않은 액세스 토큰입니다.");
        }

        // 토큰에서 이메일 추출
        String email = jwtTokenProvider.extractEmail(token);

        // 이메일로 사용자 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("VALID404_1: 사용자를 찾을 수 없습니다."));

        return member.getId();
    }
}

