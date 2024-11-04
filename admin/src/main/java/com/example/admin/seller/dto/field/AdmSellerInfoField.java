package com.example.admin.seller.dto.field;

import lombok.Getter;

@Getter
public enum AdmSellerInfoField {
    sellerName("판매자 이름"),
    sellerContact("연락처"),
    stdDt("최종 결제일"),
    blockYn("차단 여부"),
    regDt("등록일자"),
    updDt("업데이트 날짜"),
    updId("업데이트 ID"),
    blockDt("차단일자"),
    blockId("차단자")
    ;


    private final String description;

    AdmSellerInfoField(String description) {this.description = description;}
}
