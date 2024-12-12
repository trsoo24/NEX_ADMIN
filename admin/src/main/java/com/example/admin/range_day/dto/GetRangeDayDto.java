package com.example.admin.range_day.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetRangeDayDto {
    private String date;
    private String charge;
    private String reversal;
    private String refund;

    public GetRangeDayDto(String date, String charge, String reversal, String refund) {
        this.date = date;
        this.charge = charge;
        this.reversal = reversal;
        this.refund = refund;
    }
}
