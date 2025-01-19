package com.project.nupibe.domain.route.exception;

import com.project.nupibe.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RouteErrorCode implements BaseErrorCode {

    ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "Route404_0", "해당 경로를 찾을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
