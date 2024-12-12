package com.example.admin.seller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetSellerStatDto {
    private String year;
    private String month;
    private String day;

    public GetSellerStatDto(String year, String month, String day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
}
