package com.example.admin.domain.entity.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminItemSales {
    private String itemsalesNm; // 상품명
    private String merchantNm; // 회사명
    private String stdDt; // 등록일
    private String blockYn;
    private String creDt;
    private String updDt;
    private String updId;
    private String blockDt;
    private String blockId;

    public void updateUpdInfo(String updId) {
        this.updDt = today();
        this.updId = updId;
    }

    private String today() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);

        return now.format(formatter);
    }
}
