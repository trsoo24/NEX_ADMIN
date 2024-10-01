package com.example.admin.domain.dto.reconcile.gdcb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GDCBMonthlyInvoiceSum {
    private String transactionType;
    private String revsCategory;
    private String paymentType;
    private Double transactionCnt;
    private Double totalAmountSum;
    private Double revsInInvoicedCurrencySum;
    private Double chargeSum; // 수수료

    public void calculateChargeSum() {
        this.chargeSum = (double) Math.round(this.revsInInvoicedCurrencySum / this.totalAmountSum * 10 * 100) / 10.0;
    }

    public static GDCBMonthlyInvoiceSum toTransactionTypeTotal(GDCBMonthlyInvoiceSum buySum, GDCBMonthlyInvoiceSum refundSum) {
        GDCBMonthlyInvoiceSum transactionTypeTotal = GDCBMonthlyInvoiceSum.builder()
                .transactionType("B - R")
                .revsCategory(buySum.getRevsCategory())
                .paymentType(buySum.getPaymentType())
                .transactionCnt(buySum.getTransactionCnt() + refundSum.getTransactionCnt())
                .totalAmountSum(buySum.getTotalAmountSum() - refundSum.getTotalAmountSum())
                .revsInInvoicedCurrencySum(builder().revsInInvoicedCurrencySum - refundSum.getRevsInInvoicedCurrencySum())
                .chargeSum(0.0)
                .build();

        transactionTypeTotal.calculateChargeSum();

        return transactionTypeTotal;
    }

    public static GDCBMonthlyInvoiceSum toRevsCategoryTotal(GDCBMonthlyInvoiceSum sum) {
        return GDCBMonthlyInvoiceSum.builder()
                .transactionType(sum.getTransactionType())
                .revsCategory("SUM")
                .paymentType(sum.getPaymentType())
                .transactionCnt(0.0)
                .totalAmountSum(0.0)
                .revsInInvoicedCurrencySum(0.0)
                .chargeSum(0.0)
                .build();
    }

    public static GDCBMonthlyInvoiceSum generateDefault(GDCBMonthlyInvoiceSum sum, String revsCategory) {
        return GDCBMonthlyInvoiceSum.builder()
                .transactionType(sum.getTransactionType())
                .revsCategory(revsCategory)
                .paymentType(sum.getPaymentType())
                .transactionCnt(0.0)
                .totalAmountSum(0.0)
                .revsInInvoicedCurrencySum(0.0)
                .chargeSum(0.0)
                .build();
    }

    public void addTransactionCnt(double cnt) {
        this.transactionCnt += cnt;
    }

    public void addAmountSum (double amount) {
        this.totalAmountSum += amount;
    }

    public void addCurrencySum (double currency) {
        this.revsInInvoicedCurrencySum += currency;
    }
}
