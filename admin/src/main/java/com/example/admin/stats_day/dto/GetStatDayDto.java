package com.example.admin.stats_day.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetStatDayDto {
    private String year;
    private String month;
    private String day;
    private String charge;
    private String reversal;
    private String refund;

    public GetStatDayDto(String year, String month, String day, String charge, String reversal, String refund) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.charge = charge;
        this.reversal = reversal;
        this.refund = refund;
    }
}
