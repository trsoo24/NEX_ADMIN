package com.example.admin.exception.response;

import org.springframework.http.HttpStatus;

public interface ErrorResponse {
    HttpStatus getHttpStatus();
    Integer getErrorCode();
    String getErrorMessage();
}
