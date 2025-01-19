package com.project.nupibe.domain.member.controller;

import com.project.nupibe.domain.member.dto.request.RequestSignupDto;
import com.project.nupibe.domain.member.dto.request.RequestVerificationDto;
import com.project.nupibe.domain.member.dto.response.ResponseVerificationDto;
import com.project.nupibe.domain.member.service.AuthService;
import com.project.nupibe.domain.member.service.VerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final VerificationService verificationService;
    private final AuthService authService;

    public AuthController(VerificationService verificationService, AuthService authService) {
        this.verificationService = verificationService;
        this.authService = authService;
    }

    // 이메일 인증 요청 API
    @PostMapping("/requestVerification")
    public ResponseEntity<ResponseVerificationDto> requestVerification(
            @Valid @RequestBody RequestVerificationDto requestDto) {
        try {
            String expiresAt = verificationService.requestVerification(requestDto.email());
            return ResponseEntity.ok(new ResponseVerificationDto(
                    true,
                    "SUCCESS200",
                    "Verification code sent to your email.",
                    new ResponseVerificationDto.Result(expiresAt)
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseVerificationDto(
                    false,
                    "ERROR400",
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseVerificationDto(
                    false,
                    "ERROR500",
                    "Internal server error.",
                    null
            ));
        }
    }

    // 회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody RequestSignupDto signupDto) {
        try {
            authService.signup(signupDto);
            return ResponseEntity.ok("Signup successful.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error.");
        }
    }
}