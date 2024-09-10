package com.example.admin.domain.dto.range;

import com.example.admin.domain.dto.range.field.AStatRange;
import com.example.admin.domain.entity.range.RangeDay;
import lombok.*;

import java.lang.reflect.Field;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RangeDayDto {
    private String stat_day;
    private String a_stat;
    private Double b_stat;
    private Double c_stat;
    private Double d_stat;
    private Double e_stat;
    private Double f_stat;
    private Double g_stat;

    public static RangeDayDto toDto (RangeDay rangeDay) throws IllegalAccessException {
        return RangeDayDto.builder()
                .stat_day(rangeDay.getStat_day())
                .a_stat(aStatToEnum(rangeDay.getA_stat()))
                .b_stat(rangeDay.getB_stat())
                .c_stat(rangeDay.getC_stat())
                .d_stat(rangeDay.getD_stat())
                .e_stat(rangeDay.getE_stat())
                .f_stat(rangeDay.getF_stat())
                .g_stat(rangeDay.getG_stat())
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
