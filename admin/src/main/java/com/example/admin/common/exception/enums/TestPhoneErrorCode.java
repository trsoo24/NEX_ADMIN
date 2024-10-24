package com.example.admin.common.exception.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TestPhoneErrorCode implements ErrorCode {
    DUPLICATED_CTN(HttpStatus.BAD_REQUEST, 400, "중복된 차단 번호입니다."),
    NOT_IN_DB(HttpStatus.BAD_REQUEST, 400, "존재하지 않는 CTN 입니다")
    ;

    private final HttpStatus httpStatus;
    private final Integer errorCode;
    private final String message;
}

