package com.example.admin.reconcile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GDCBDetailCompare {
    // 공통
    private String revsCategory; // APP, APP_SUBSCRIPTION, CONTENT, NA, SPECIAL_APP, 전체
    private Double totalAmountSum;

    // TB_GDCB_MONTHLY_INVOICE_SUM
    private String tYear;
    private String tMonth;
    private String parameterType;
    private String transactionType; // B : 구매 , R : 환불
    private String paymentType; // 00 : DCB , 99 : 기타 , PG : 소액결제, 전체
    private Double transactionCnt;
    private Double revsInInvoicedCurrencySum;
    private Double chargeSum;

    // G_GDCB_MONTHLY_INVOICE_SUM
    private String gYear;
    private String gMonth;
    private Double productPriceSum;
    private Double taxSum;
    private Double revShareSum;

    public static GDCBDetailCompare fromTbMonthlyInvoiceSum(String month, String parameterType, GDCBMonthlyInvoiceSum sum) {
        String[] monthArray = month.split("-");

        return GDCBDetailCompare.builder()
                .parameterType(parameterType)
                .revsCategory(sum.getRevsCategory())
                .totalAmountSum(sum.getTotalAmountSum())
                .tYear(monthArray[0])
                .tMonth(monthArray[1])
                .paymentType(sum.getPaymentType())
                .transactionType(transTransactionTypeToDescription(sum.getTransactionType()))
                .transactionCnt(sum.getTransactionCnt())
                .revsInInvoicedCurrencySum(sum.getRevsInInvoicedCurrencySum())
                .chargeSum(sum.getChargeSum())
                .build();
    }

    public static GDCBDetailCompare fromGMonthlyInvoiceSum(String month, GoogleMonthlyInvoiceSum sum) {
        String[] monthArray = month.split("-");

        return GDCBDetailCompare.builder()
                .gYear(monthArray[0])
                .gMonth(monthArray[1])
                .revsCategory(sum.getRevsCategory())
                .totalAmountSum(sum.getTotalAmountSum())
                .productPriceSum(sum.getProductPriceSum())
                .taxSum(sum.getTaxSum())
                .revShareSum(sum.getRevShareSum())
                .build();
    }

    private static String transTransactionTypeToDescription(String transactionType) {
        if (transactionType.equals("B")) {
            return "Charge";
        } else if (transactionType.equals("C")) {
            return "Refund";
        } else {
            return transactionType;
        }
    }
}
