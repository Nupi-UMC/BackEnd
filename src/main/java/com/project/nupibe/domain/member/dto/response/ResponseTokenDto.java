package com.project.nupibe.domain.member.dto.response;

public record ResponseTokenDto(
        String accessToken,
        String refreshToken,
        long expiresIn
) {}
