package com.project.nupibe.domain.member.exception.code;

import com.project.nupibe.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum VerificationErrorCode implements BaseErrorCode {

    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "400", "유효하지 않은 이메일입니다."),
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "500", "이메일 전송에 실패했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}