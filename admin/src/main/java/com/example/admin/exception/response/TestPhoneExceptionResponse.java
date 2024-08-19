package com.example.admin.exception.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class TestPhoneExceptionResponse implements ErrorResponse{
    private HttpStatus httpStatus;
    private Integer errorCode;
    private String errorMessage;
}
