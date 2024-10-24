package com.example.admin.reconcile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertReconcileDto {
    private String yearMonth;
    private String fileType;
    private String fileCode;
    private String fileName;
    private String filePath;
    private String result;
}
