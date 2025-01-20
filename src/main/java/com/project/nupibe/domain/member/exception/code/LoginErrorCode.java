package com.project.nupibe.domain.member.exception.code;

import com.project.nupibe.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LoginErrorCode implements BaseErrorCode {
    EMAIL_OR_PASSWORD_INVALID(HttpStatus.UNAUTHORIZED, "ERROR401", "이메일 또는 비밀번호가 일치하지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR500", "Internal server error.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}