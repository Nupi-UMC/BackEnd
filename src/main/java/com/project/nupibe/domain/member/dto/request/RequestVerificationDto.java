package com.project.nupibe.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record RequestVerificationDto(
        @NotNull(message = "Email is required.")
        @Email(message = "Invalid email format.")
        String email
) {}