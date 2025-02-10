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

import java.io.IOException;

@Service
public class SignupService {

    private final MemberRepository memberRepository;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    private final S3UploadService s3UploadService;

    public SignupService(MemberRepository memberRepository, RedisService redisService, PasswordEncoder passwordEncoder, S3UploadService s3UploadService) {
        this.memberRepository = memberRepository;
        this.redisService = redisService;
        this.passwordEncoder = passwordEncoder;
        this.s3UploadService = s3UploadService;
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
            try {
                return s3UploadService.saveFile(profileImage); // S3 업로드 후 URL 반환
            } catch (IOException e) {
                throw new RuntimeException("프로필 이미지 업로드 실패", e);
            }
        }
        return "https://your-default-image.com/default.png"; // 기본 프로필 이미지
    }
}