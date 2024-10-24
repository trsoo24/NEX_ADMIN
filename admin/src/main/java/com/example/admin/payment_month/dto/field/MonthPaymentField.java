package com.example.admin.payment_month.dto.field;

import lombok.Getter;

@Getter
public enum MonthPaymentField {
    statMonth( "월별"),
//    statDay("일별"),
    totalAmount("결제 금액 합"),
    buyAmount("구매 금액"),
    buyAmountPercent("구매 비율"),
    cancelAmount("취소 금액"),
    cancelAmountPercent("취소 비율"),
    refundAmount("환불 금액"),
    refundAmountPercent("환불 비율"),
    paymentCount("결제 건수 합"),
    buyCount("구매 건수"),
    buyCountPercent("구매 건수 비율"),
    cancelCount("취소 건수"),
    cancelCountPercent("취소 건수 비율"),
    refundCount("환불 건수"),
    refundCountPercent("환불 건수 비율"),
    buyAmountAverage("구매 건별 평균 금액"),
    cancelAmountAverage("취소 건별 평균 금액"),
    refundAmountAverage("환불 건별 평균 금액");
    private final String description;

    MonthPaymentField(String description) {
        this.description = description;
    }
}
