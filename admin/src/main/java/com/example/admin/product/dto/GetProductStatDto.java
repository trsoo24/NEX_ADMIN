package com.example.admin.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetProductStatDto {
    private String year;
    private String month;
    private String day;

    public GetProductStatDto(String year, String month, String day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
}
