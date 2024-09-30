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
        this.chargeSum = (double) Math.round(this.revsInInvoicedCurrencySum / this.totalAmountSum * 10  * 100) / 10.0;
    }

    public static GDCBMonthlyInvoiceSum toTotal(GDCBMonthlyInvoiceSum buySum, GDCBMonthlyInvoiceSum refundSum) {
        return null;
    }
}
