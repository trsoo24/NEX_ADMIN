package com.example.admin.reconcile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyInvoiceSum { // google Invoice Detail 파일 값 합계
    private String year;
    private String month;
    private String transactionType; // B : 결제 , R : 환불
    private String paymentType; // 결제 구분 A. 00 : 기존결재 {DCB 로 예상} B. PG : 소액결재 C. 99 : 기타
    private String revsCategory; // 결제 유형 ( APP, APP_SUBSCRIPTION, CONTENT, NA (기타), SPECIAL_APP )
    private Double cnt;
    private Double itemPriceSum; // 아이템 금액 합
    private Double taxSum; // 세금 합
    private Double totalAmountSum; // 총 금액 합
    private Double revsInInvoicedCurrencySum; // 유플러스 매출 합

    public static MonthlyInvoiceSum toEntity(String year, String month, String transactionType,
                                             String paymentType, String revsCategory, Double cnt, Double itemPriceSum,
                                             Double taxSum, Double totalAmountSum, Double revsInInvoicedCurrencySum) {
        return MonthlyInvoiceSum.builder()
                .year(year)
                .month(month)
                .transactionType(transactionType)
                .paymentType(paymentType)
                .revsCategory(revsCategory)
                .cnt(cnt)
                .itemPriceSum(itemPriceSum)
                .taxSum(taxSum)
                .totalAmountSum(totalAmountSum)
                .revsInInvoicedCurrencySum(revsInInvoicedCurrencySum)
                .build();
    }
}


/** TODO 대사 정산 API 정리
 * DCB + 소액결제 + 기타 총합의 SUM(구매 + 환불) , 구매 , 환불 을 나누어 REVS_CATEGORY 별 금액 값이 있어야함
 * DCB , 소액결제 , 기타 각각 SUM(구매 + 환불) , 구매 , 환불을 나누어 REVS_CATEGORY 별 금액 값이 있어야함
 * 검색한 달 + 지난 달 값이 같이 있어야함
 */