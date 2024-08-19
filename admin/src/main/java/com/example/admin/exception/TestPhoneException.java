package com.example.admin.exception;

import com.example.admin.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class TestPhoneException extends RuntimeException {
    private final ErrorCode errorCode;

    public TestPhoneException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
