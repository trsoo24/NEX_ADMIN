package com.example.admin.billing_grade.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetBillingGradeDto {
    private String resultCode;
    private String firstDay;
    private String lastDay;
    private String charge;
    private String reversal;
    private String refund;
    private String paid;
    private String unpaid;

    public GetBillingGradeDto(String resultCode, String firstDay, String lastDay, String charge, String reversal, String refund, String paid, String unpaid) {
        this.resultCode = resultCode;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.charge = charge;
        this.reversal = reversal;
        this.refund = refund;
        this.paid = paid;
        this.unpaid = unpaid;
    }
}
