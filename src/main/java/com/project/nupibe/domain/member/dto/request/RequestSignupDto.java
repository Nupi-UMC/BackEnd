package com.project.nupibe.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RequestSignupDto(
        @NotNull(message = "Verification ID is required.")
        Long verificationId,

        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email format.")
        String email,

        @NotBlank(message = "Password is required.")
        String password,

        @NotBlank(message = "Nickname is required.")
        String nickname,

        String profile
) {}