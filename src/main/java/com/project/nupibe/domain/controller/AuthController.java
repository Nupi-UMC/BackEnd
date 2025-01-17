package com.project.nupibe.domain.controller;

import com.project.nupibe.domain.dto.request.RequestVerificationDto;
import com.project.nupibe.domain.dto.response.ResponseVerificationDto;
import com.project.nupibe.domain.service.VerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final VerificationService verificationService;

    public AuthController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

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
}