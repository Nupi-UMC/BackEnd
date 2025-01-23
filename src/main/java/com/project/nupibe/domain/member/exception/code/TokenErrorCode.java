package com.project.nupibe.domain.member.exception.code;

import com.project.nupibe.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TokenErrorCode implements BaseErrorCode {
    REFRESH_TOKEN_MISSING(HttpStatus.BAD_REQUEST, "ERROR400", "Refresh Token이 누락되었습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "ERROR401", "유효하지 않은 Refresh Token입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR500", "Internal server error.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}