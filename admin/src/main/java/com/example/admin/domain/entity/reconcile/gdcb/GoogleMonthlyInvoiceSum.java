package com.example.admin.domain.entity.reconcile.gdcb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleMonthlyInvoiceSum { // Google Monthly invoice fees and expense summary LGU DCB YYYYMM.csv 파일 내용 저장
    private String year;
    private String month;
    private String revsCategory;
    private Double itemPriceSum;
    private Double taxSum;
    private Double totalAmountSum;
    private Double revShareSum;
}
