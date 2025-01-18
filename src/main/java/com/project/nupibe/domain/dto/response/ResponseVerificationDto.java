package com.project.nupibe.domain.dto.response;

public record ResponseVerificationDto(
        boolean isSuccess,
        String code,
        String message,
        Result result
) {
    public static record Result(String expiresAt) {}
}