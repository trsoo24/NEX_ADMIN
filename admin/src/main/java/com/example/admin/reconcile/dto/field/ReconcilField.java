package com.example.admin.reconcile.dto.field;

import lombok.Getter;

@Getter
public enum ReconcilField {
    CREATE_DATE("구매년월"),
    FILE_TYPE("파일 구분"),
    RECONCIL_ID("ID"),
    FILE_NAME("파일명"),
    FILE_PATH("파일 경로"),
    RESULT_CODE("작업 결과")
    ;

    private final String description;

    ReconcilField(String description) {
        this.description = description;
    }
}
