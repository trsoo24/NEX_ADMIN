package com.example.admin.exception;

import com.example.admin.exception.response.MemberExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NexExceptionHandler {
    @ExceptionHandler(MemberException.class)
    public ResponseEntity<MemberExceptionResponse> handleMemberException(MemberException ex){
        MemberExceptionResponse memberExceptionResponse = MemberExceptionResponse.builder()
                .httpStatus(ex.getErrorCode().getHttpStatus())
                .errorCode(ex.getErrorCode().getErrorCode())
                .errorMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(memberExceptionResponse);
    }
}
