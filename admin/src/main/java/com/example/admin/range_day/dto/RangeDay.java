package com.example.admin.range_day.dto;

import lombok.*;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RangeDay {
    private String stat_day;
    private String a_stat;
    private Double b_stat;
    private Double c_stat;
    private Double d_stat;
    private Double e_stat;
    private Double f_stat;
    private Double g_stat;
    private final String dcb = "GDCB";

    public static RangeDay setDefault(String date, String aStat) {
        double def = 0;
        return RangeDay.builder()
                .stat_day(date)
                .a_stat(aStat)
                .b_stat(def)
                .c_stat(def)
                .d_stat(def)
                .e_stat(def)
                .f_stat(def)
                .g_stat(def)
                .build();
    }

    public void addTotalValue(RangeDay rangeDay) {
        this.b_stat += rangeDay.getB_stat();
        this.c_stat += rangeDay.getC_stat();
        this.d_stat += rangeDay.getD_stat();
        this.e_stat += rangeDay.getE_stat();
        this.f_stat += rangeDay.getF_stat();
        this.g_stat += rangeDay.getG_stat();
    }
}
