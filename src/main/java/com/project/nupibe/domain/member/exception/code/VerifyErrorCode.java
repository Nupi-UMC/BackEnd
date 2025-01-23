package com.project.nupibe.domain.member.exception.code;

import com.project.nupibe.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum VerifyErrorCode implements BaseErrorCode {
    EMAIL_OR_CODE_MISSING(HttpStatus.BAD_REQUEST, "ERROR400", "이메일 또는 인증코드가 누락되었습니다."),
    CODE_MISMATCH(HttpStatus.UNAUTHORIZED, "ERROR401", "인증코드가 일치하지 않습니다."),
    CODE_EXPIRED(HttpStatus.UNAUTHORIZED, "ERROR401", "인증코드가 만료되었습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "ERROR404", "등록된 이메일을 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR500", "Internal server error.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}