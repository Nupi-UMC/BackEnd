package com.project.nupibe.domain.member.service;

import com.project.nupibe.domain.member.dto.request.RequestSignupDto;
import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.exception.code.SignupErrorCode;
import com.project.nupibe.domain.member.exception.handler.MemberException;
import com.project.nupibe.domain.member.repository.MemberRepository;
import com.project.nupibe.domain.member.redis.RedisService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public Long signup(RequestSignupDto signupDto, MultipartFile profileImage) {
        // 이메일 중복 체크
        if (memberRepository.findByEmail(signupDto.getEmail()).isPresent()) {
            throw new MemberException(SignupErrorCode.EMAIL_ALREADY_REGISTERED);
        }

        // 이메일 인증 여부 확인
        if (!redisService.isVerified(signupDto.getEmail())) {
            throw new MemberException(SignupErrorCode.EMAIL_NOT_VERIFIED);
        }

        // 프로필 이미지 처리
        String profileImageUrl = uploadProfileImage(profileImage);

        // 사용자 생성 및 저장
        Member member = Member.builder()
                .email(signupDto.getEmail())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .nickname(signupDto.getNickname())
                .profile(profileImageUrl) // 프로필 이미지 URL 저장
                .isVerified(true) // 인증된 회원
                .build();

        return memberRepository.save(member).getId();
    }

    private String uploadProfileImage(MultipartFile profileImage) {
        if (profileImage != null && !profileImage.isEmpty()) {
            // S3 또는 로컬 저장소에 업로드하는 로직 (여기서는 기본값 설정)
            return "https://your-s3-bucket.com/" + profileImage.getOriginalFilename();
        }
        return "https://your-default-image.com/default.png"; // 기본 프로필 이미지
    }
}