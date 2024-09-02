package com.example.admin.domain.dto.merchant.field;

import lombok.Getter;

@Getter
public enum AdmMerchantInfoField {
    merchantNm("판매자 이름"),
    merchantContact("연락처"),
    stdDt("최종 결제일"),
    blockYn("차단 여부"),
    regDt("등록일자"),
    updDt("업데이트 날짜"),
    updId("업데이트 ID"),
    blockDt("차단일자"),
    blockId("차단자")
    ;


    private final String description;

    AdmMerchantInfoField(String description) {this.description = description;}
}
