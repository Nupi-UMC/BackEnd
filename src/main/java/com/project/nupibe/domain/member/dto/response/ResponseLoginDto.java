package com.project.nupibe.domain.member.dto.response;

public record ResponseLoginDto(
        String accessToken,
        String refreshToken,
        long expiresIn
) {}