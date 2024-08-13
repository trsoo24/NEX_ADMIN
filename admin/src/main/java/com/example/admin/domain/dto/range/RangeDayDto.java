package com.example.admin.domain.dto.range;

import com.example.admin.domain.dto.range.field.AStatRange;
import com.example.admin.domain.entity.range.RangeDay;
import com.example.admin.domain.entity.range.RangeDay;
import lombok.*;

import java.lang.reflect.Field;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RangeDayDto {
    private String statDay;
    private String a_stat;
    private Double b_stat;
    private Double c_stat;
    private Double d_stat;
    private Double e_stat;
    private Double f_stat;
    private Double g_stat;

    public static RangeDayDto toDto (RangeDay rangeDay) throws IllegalAccessException {
        return RangeDayDto.builder()
                .statDay(rangeDay.getStatDay())
                .a_stat(aStatToEnum(rangeDay.getAStat()))
                .b_stat(rangeDay.getBStat())
                .c_stat(rangeDay.getCStat())
                .d_stat(rangeDay.getDStat())
                .e_stat(rangeDay.getEStat())
                .f_stat(rangeDay.getFStat())
                .g_stat(rangeDay.getGStat())
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
