package com.example.admin.common.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DataResult<T> extends StatusResult {
    private T data; // 단일 객체

    public DataResult(Boolean success, T data) {
        super(success);
        this.data = data;
    }
}
