package com.example.admin.exception.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    DUPLICATED_CTN(HttpStatus.BAD_REQUEST, 400, "중복된 전화 번호입니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, 400, "중복된 이메일입니다."),
    DUPLICATED_NAME(HttpStatus.BAD_REQUEST, 400, "이미 존재하는 계정입니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, 400, "찾을 수 없는 사용자입니다.")
    ;

    private final HttpStatus httpStatus;
    private final Integer errorCode;
    private final String message;
}
