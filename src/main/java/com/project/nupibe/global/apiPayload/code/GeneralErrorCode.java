package com.project.nupibe.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum GeneralErrorCode implements BaseErrorCode{

    // 일반적인 ERROR 응답 (다 만들지 않으셔도 됩니다.)
    BAD_REQUEST_400(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다"),
    UNAUTHORIZED_401(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다"),
    FORBIDDEN_403(HttpStatus.FORBIDDEN, "COMMON403", "접근이 금지되었습니다"),
    NOT_FOUND_404(HttpStatus.NOT_FOUND, "COMMON404", "요청한 자원을 찾을 수 없습니다"),
    INTERNAL_SERVER_ERROR_500(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 내부 오류가 발생했습니다"),

    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),


    // 유효성 검사
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "VALID400_0", "잘못된 파라미터 입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "VALID400_1", "유효하지 않은 액세스 토큰입니다."),


    // 경로 관련
    MINIMUM_STORES_REQUIRED(HttpStatus.BAD_REQUEST, "ROUTE400_1", "최소 2개 이상의 장소가 필요합니다."),
    SUMMARY_APIDATA_NOT_LOAD(HttpStatus.NOT_FOUND, "ROUTE400_2", "카카오 API 응답에 summary 데이터가 없습니다. 주변 교통상황 문제일 수 있으니 다른 장소로 다시 한번만 시도바랍니다.");


    // 필요한 필드값 선언
    private final HttpStatus status;
    private final String code;
    private final String message;
}
