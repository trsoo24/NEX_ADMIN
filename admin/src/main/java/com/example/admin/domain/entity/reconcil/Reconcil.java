package com.example.admin.domain.entity.reconcil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Reconcil {
    // 구매년월 / 파일구분 / ID / 파일명 / 파일 경로 / 작업결과 / 시작 시간 / 종료 시간
    private String createDate;
    private String fileType;
    private String reconcilId;
    private String fileName;
    private String filePath;
    private String resultCode;
    private String startDate;
    private String endDate;
}
