package com.project.nupibe.domain.member.dto.response;

public record ResponseSignupDto(boolean isSuccess, String code, String message, Result result) {
    public static record Result(Long userId) {}
}