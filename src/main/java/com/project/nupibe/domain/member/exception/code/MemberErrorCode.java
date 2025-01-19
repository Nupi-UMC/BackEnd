package com.project.nupibe.domain.member.exception.code;

import com.project.nupibe.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND,
            "MEMBER404",
                    "사용자를 찾을 수 없습니다."),
            ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
