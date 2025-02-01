package com.project.nupibe.domain.store.exception.code;

import com.project.nupibe.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum StoreErrorCode implements BaseErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND,
            "STORE404",
                    "가게를 찾을 수 없습니다."),
    ALREADY_EXISTS(HttpStatus.CONFLICT,
            "STORE409",
            "이미 가게가 존재합니다."),
    UNSUPPORTED_QUERY(HttpStatus.BAD_REQUEST,
            "STORE401",
            "지원하지 않는 쿼리입니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE405", "해당 가게의 이미지가 존재하지 않습니다.");

    ;



    private final HttpStatus status;
    private final String code;
    private final String message;
}
