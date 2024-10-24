package com.example.admin.range_month.dto;

import com.example.admin.range_day.dto.field.AStatRange;
import lombok.*;

import java.lang.reflect.Field;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RangeMonthDto {
    private String stat_month;
    private String a_stat;
    private Double b_stat;
    private Double c_stat;
    private Double d_stat;
    private Double e_stat;
    private Double f_stat;
    private Double g_stat;

    public static RangeMonthDto toDto (RangeMonth rangeMonth) throws IllegalAccessException {
        return RangeMonthDto.builder()
                .stat_month(rangeMonth.getStat_month())
                .a_stat(aStatToEnum(rangeMonth.getA_stat()))
                .b_stat(rangeMonth.getB_stat())
                .c_stat(rangeMonth.getC_stat())
                .d_stat(rangeMonth.getD_stat())
                .e_stat(rangeMonth.getE_stat())
                .f_stat(rangeMonth.getF_stat())
                .g_stat(rangeMonth.getG_stat())
                .build();
    }

    private static String aStatToEnum(String aStat) throws IllegalAccessException {
        Field[] enumFields = AStatRange.class.getDeclaredFields();

        for (Field enumField : enumFields) {
            if (((AStatRange) enumField.get(null)).getA_stat().equals(aStat)) {
                return ((AStatRange) enumField.get(null)).getRange();
            }
        }
        throw new IllegalAccessException();
    }
}
