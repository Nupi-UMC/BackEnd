package com.project.nupibe.domain.member.exception.code;

import com.project.nupibe.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SignupErrorCode implements BaseErrorCode {
    REQUIRED_FIELD_MISSING(HttpStatus.BAD_REQUEST, "ERROR400", "필수 필드가 누락되었습니다."),
    EMAIL_NOT_VERIFIED(HttpStatus.UNAUTHORIZED, "ERROR401", "인증되지 않은 이메일입니다."),
    EMAIL_ALREADY_REGISTERED(HttpStatus.CONFLICT, "ERROR409", "이미 가입된 이메일입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR500", "Internal server error.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}