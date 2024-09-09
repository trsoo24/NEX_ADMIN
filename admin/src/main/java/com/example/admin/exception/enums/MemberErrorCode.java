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
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, 400, "찾을 수 없는 사용자입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, 400, "유효하지 않은 토큰입니다."),
    UNACCEPTABLE_ROLE(HttpStatus.BAD_REQUEST, 400, "허가되지 않은 권한입니다."),
    INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST, 400, "비밀번호 오류입니다.")
    ;

    private final HttpStatus httpStatus;
    private final Integer errorCode;
    private final String message;
}
