package com.example.admin.domain.dto.payment.field;

import lombok.Getter;

@Getter
public enum PayDetailField {
    ctn("CTN"),
    purchaseName("결제일"),
    cancelDate("취소일"),
    price("결제금액"),
    paymentType("결제 수단"),
    productName("상품명"),
    company("회사명"),
    merchantName("판매자명")
    ;

    private final String description;

    PayDetailField(String description) {
        this.description = description;
    }
}
