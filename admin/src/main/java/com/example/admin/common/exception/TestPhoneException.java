package com.example.admin.common.exception;

import com.example.admin.common.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class TestPhoneException extends RuntimeException {
    private final ErrorCode errorCode;

    public TestPhoneException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
