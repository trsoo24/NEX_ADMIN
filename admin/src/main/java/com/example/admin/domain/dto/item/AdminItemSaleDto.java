package com.example.admin.domain.dto.item;

import com.example.admin.domain.entity.item.AdminItemSales;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminItemSaleDto {
    private String dcb;
    private String itemsalesNm; // 상품명
    private String merchantNm; // 회사명
    private String blockYn;
    private String blockDt;
    private String blockId;


    public AdminItemSales toNewAdminItemSale() {
        AdminItemSales.AdminItemSalesBuilder adminItemSales = AdminItemSales.builder()
                .itemsalesNm(this.itemsalesNm)
                .merchantNm(this.merchantNm)
                .stdDt(today())
                .blockYn(this.blockYn)
                .creDt(today())
                .updDt("")
                .updId("");

        if ("Y".equals(this.blockYn)) {
            adminItemSales.blockDt(this.blockDt)
                    .blockId(this.blockId);
        } else {
            adminItemSales.blockDt("")
                    .blockId("");
        }

        return adminItemSales.build();
    }

    private String today() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);

        return now.format(formatter);
    }
}
