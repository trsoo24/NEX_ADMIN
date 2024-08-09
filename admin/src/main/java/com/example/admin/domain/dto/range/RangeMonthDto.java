package com.example.admin.domain.dto.range;

import com.example.admin.domain.dto.range.field.AStatRange;
import com.example.admin.domain.entity.range.RangeMonth;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RangeMonthDto {
    private String aStat;
    private Double bStat;
    private Double cStat;
    private Double dStat;
    private Double eStat;
    private Double fStat;
    private Double gStat;

    public static RangeMonthDto toDto (RangeMonth rangeMonth) throws IllegalAccessException {
        return RangeMonthDto.builder()
                .aStat(aStatToEnum(rangeMonth.getAStat()))
                .bStat(rangeMonth.getBStat())
                .cStat(rangeMonth.getCStat())
                .dStat(rangeMonth.getDStat())
                .eStat(rangeMonth.getEStat())
                .fStat(rangeMonth.getFStat())
                .gStat(rangeMonth.getGStat())
                .build();
    }

    private static String aStatToEnum(String aStat) throws IllegalAccessException {
        Field[] enumFields = AStatRange.class.getDeclaredFields();

        for (Field enumField : enumFields) {
            if (((AStatRange) enumField.get(null)).getAStat().equals(aStat)) {
                return ((AStatRange) enumField.get(null)).getRange();
            }
        }
        throw new IllegalAccessException();
    }
}
