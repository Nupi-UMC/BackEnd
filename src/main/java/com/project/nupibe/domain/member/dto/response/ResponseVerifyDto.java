package com.project.nupibe.domain.member.dto.response;

public record ResponseVerifyDto(
        boolean isSuccess,
        String code,
        String message,
        Result result
) {
    public static record Result(Long verificationId) {}
}