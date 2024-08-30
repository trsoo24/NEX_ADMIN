package com.example.admin.domain.dto.merchant.field;

import lombok.Getter;

@Getter
public enum AdmMerchantInfoField {
    regDt("등록일자"),
    merchantNm("판매자 이름"),
    stdDt("최종 결제일"),
    merchantContact("연락처"),
    blockDt("차단일자"),
    blockId("차단자")
    ;


    private final String description;

    AdmMerchantInfoField(String description) {this.description = description;}
}
