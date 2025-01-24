package com.project.nupibe.domain.member.service;

import com.project.nupibe.domain.member.redis.RedisService;
import com.project.nupibe.domain.member.exception.code.VerificationErrorCode;
import com.project.nupibe.domain.member.exception.handler.MemberException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class VerificationService {

    private final RedisService redisService;
    private final JavaMailSender mailSender;

    public VerificationService(RedisService redisService, JavaMailSender mailSender) {
        this.redisService = redisService;
        this.mailSender = mailSender;
    }

    public String requestVerification(String email) {
        // 이메일 유효성 검증
        if (email == null || !email.contains("@")) {
            throw new MemberException(VerificationErrorCode.INVALID_EMAIL);
        }

        // 인증 코드 생성
        String verificationCode = generateVerificationCode();
        long ttl = 300; // 5분

        // Redis에 인증 코드 저장
        redisService.saveVerificationCode(email, verificationCode, ttl);

        // 이메일 전송
        try {
            sendVerificationEmail(email, verificationCode);
        } catch (Exception e) {
            throw new MemberException(VerificationErrorCode.EMAIL_SEND_FAILED);
        }

        // 인증 코드 만료 시간 반환
        return LocalDateTime.now().plusSeconds(ttl).toString();
    }

    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999)); // 6자리 숫자
    }

    private void sendVerificationEmail(String email, String verificationCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("[NUPI] 인증번호");
        helper.setText(
                "<p>인증번호: <strong>" + verificationCode + "</strong></p>" +
                        "<p>인증번호 유효시간은 5분입니다.</p>",
                true
        );

        mailSender.send(message);
    }
}
