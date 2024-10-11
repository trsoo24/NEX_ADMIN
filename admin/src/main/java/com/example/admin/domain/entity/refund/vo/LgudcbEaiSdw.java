package com.example.admin.domain.entity.refund.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LgudcbEaiSdw {
    /**
     * EAI Action (insert)
     */
    private String cmd;
    /**
     * 요청에대한고유ID
     */
    private String newCorrelationId;
    /**
     * 결제유형
     */
    private String newAccountType;
    /**
     * 서비스코드
     */
    private String newSmlsStlmDvCd;
    /**
     * 결제대행사서비스코드
     */
    private String newSmlsStlmCmpnyCd;
    /**
     * 청구서번호
     */
    private String newBan;
    /**
     * 가입계약번호
     */
    private String newAceNo;
    /**
     * 처리년월
     */
    @Setter
    private String newPrssYymm;
    /**
     * 청구처리
     */
    private String newRequestType;
    /**
     * 상품가격
     */
    private long newItemPrice;
    /**
     * 상품세금
     */
    private long newTax;
    /**
     * 거래금액
     */
    private long newTotal;
    /**
     * 구입거래시간
     */
    private String newAuthDate;
    /**
     * 배치거래날짜
     */
    private String newTransactionDate;
    /**
     * 판매자
     */
    private String newMerchantName;
    /**
     * 상품명
     */
    private String newItemName;
    /**
     * 판매자연락처
     */
    private String newMerchantContact;
    /**
     * CTN
     */
    private String newProdNo;
}