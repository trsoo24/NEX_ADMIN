package com.example.admin.product.dto.field;

import lombok.Getter;

@Getter
public enum ProductInfoField {
    productName("상품명"),
    sellerCompany("판매자명"),
    stdDt("최종 결제일");

    private final String description;

    ProductInfoField(String description) {
        this.description = description;
    }
}
