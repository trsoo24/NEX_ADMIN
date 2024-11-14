package com.example.admin.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private String productName; // 상품명
    private String stdDt; // 마지막 구매 일자
    private Double price;
    private String sellerCompany; // 판매 회사 명
    private String sellerName; // 판매자명
    private Boolean blockYn;
    private String regDt;
    private String regId;
    private String updDt;
    private String updId;
    private String blockDt;
    private String blockId;
    private final String dcb = "GDCB";
}
