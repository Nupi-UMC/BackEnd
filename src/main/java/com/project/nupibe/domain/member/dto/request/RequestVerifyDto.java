package com.project.nupibe.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record RequestVerifyDto(
        @NotNull(message = "Email is required.")
        @Email(message = "Invalid email format.")
        String email,
        @NotNull(message = "Verification code is required.")
        String verificationCode
) {}