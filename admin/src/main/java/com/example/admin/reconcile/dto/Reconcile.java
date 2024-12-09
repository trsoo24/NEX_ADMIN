package com.example.admin.reconcile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Reconcile {
    private String yearMonth;
    private String fileType;
    private String fileCode;
    private String fileName;
    private String filePath;
    private String result;
    private String createDt;
    private String updateDt;

    public void setDateFormat() {
        this.createDt = formatDateToDate(this.createDt);
        this.updateDt = formatDateToDate(this.updateDt);
    }

    private String formatDateToDate(String date) {
        date = date.split("\\.")[0];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return String.valueOf(LocalDateTime.parse(date, formatter));
    }
}
