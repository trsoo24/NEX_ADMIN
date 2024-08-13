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
    private String statDay;
    private String aStat;
    private Double bStat;
    private Double cStat;
    private Double dStat;
    private Double eStat;
    private Double fStat;
    private Double gStat;

    public void calculateFStat() {
        this.fStat = this.dStat - this.eStat;
    }

    public void addValue(double value) {
        this.bStat++;
        this.cStat++;
        if (value >= 0) {
            this.dStat += value;
        } else {
            this.eStat += value;
        }
        this.fStat += value;
    }

    public void calculateGStat() {
        this.gStat = this.dStat / this.bStat;
    }

    public void roundDFG() {
        this.dStat = (double) Math.round(this.dStat);
        this.fStat = (double) Math.round(this.fStat);
        this.gStat = (double) Math.round(this.gStat);
    }

    public static RangeDay setDefault(String date, String aStat) {
        double def = 0;
        return RangeDay.builder()
                .statDay(date)
                .aStat(aStat)
                .bStat(def)
                .cStat(def)
                .dStat(def)
                .eStat(def)
                .fStat(def)
                .gStat(def)
                .build();
    }
}
