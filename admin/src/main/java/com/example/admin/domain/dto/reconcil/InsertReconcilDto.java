package com.example.admin.domain.dto.reconcil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertReconcilDto {
    private String date;
    private String fileType;
    private String id;
    private String fileName;
    private String resultCode;
    private String startDate;
    private String endDate;
    private String dcb;
}
