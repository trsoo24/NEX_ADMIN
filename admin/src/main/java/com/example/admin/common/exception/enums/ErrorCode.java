package com.example.admin.common.exception.enums;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus getHttpStatus();
    Integer getErrorCode();
    String getMessage();
}
