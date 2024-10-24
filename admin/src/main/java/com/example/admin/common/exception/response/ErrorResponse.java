package com.example.admin.common.exception.response;

import org.springframework.http.HttpStatus;

public interface ErrorResponse {
    HttpStatus getHttpStatus();
    Integer getErrorCode();
    String getErrorMessage();
}
