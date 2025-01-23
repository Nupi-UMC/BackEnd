package com.project.nupibe.domain.member.exception.code;

import com.project.nupibe.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LogoutErrorCode implements BaseErrorCode {
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "ERROR401", "유효하지 않은 Access Token입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR500", "Internal server error.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}