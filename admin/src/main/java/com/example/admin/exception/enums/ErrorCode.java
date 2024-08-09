package com.example.admin.exception.enums;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus getHttpStatus();
    Integer getErrorCode();
    String getMessage();
}
