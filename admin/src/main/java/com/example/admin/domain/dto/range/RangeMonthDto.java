package com.example.admin.domain.dto.range;

import com.example.admin.domain.dto.range.field.AStatRange;
import com.example.admin.domain.entity.range.RangeMonth;
import lombok.*;

import java.lang.reflect.Field;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RangeMonthDto {
    private String statMonth;
    private String a_stat;
    private Double b_stat;
    private Double c_stat;
    private Double d_stat;
    private Double e_stat;
    private Double f_stat;
    private Double g_stat;

    public static RangeMonthDto toDto (RangeMonth rangeMonth) throws IllegalAccessException {
        return RangeMonthDto.builder()
                .statMonth(rangeMonth.getStatMonth())
                .a_stat(aStatToEnum(rangeMonth.getAStat()))
                .b_stat(rangeMonth.getBStat())
                .c_stat(rangeMonth.getCStat())
                .d_stat(rangeMonth.getDStat())
                .e_stat(rangeMonth.getEStat())
                .f_stat(rangeMonth.getFStat())
                .g_stat(rangeMonth.getGStat())
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
