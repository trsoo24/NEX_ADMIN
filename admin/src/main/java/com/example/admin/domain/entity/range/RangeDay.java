package com.example.admin.domain.entity.range;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RangeDay {
    private String stat_day;
    private String a_stat;
    private Double b_stat;
    private Double c_stat;
    private Double d_stat;
    private Double e_stat;
    private Double f_stat;
    private Double g_stat;
    private String dcb;

    public void calculateFStat() {
        this.f_stat = this.d_stat - this.e_stat;
    }

    public void addValue(double value) {
        this.b_stat++;
        this.c_stat++;
        if (value >= 0) {
            this.d_stat += value;
        } else {
            this.e_stat += value;
        }
        this.f_stat += value;
    }

    public void calculateGStat() {
        this.g_stat = this.d_stat / this.b_stat;
    }

    public void roundDFG() {
        this.d_stat = (double) Math.round(this.d_stat);
        this.f_stat = (double) Math.round(this.f_stat);
        this.g_stat = (double) Math.round(this.g_stat);
    }

    public static RangeDay setDefault(String date, String aStat, String dcb) {
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
                .dcb(dcb)
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
