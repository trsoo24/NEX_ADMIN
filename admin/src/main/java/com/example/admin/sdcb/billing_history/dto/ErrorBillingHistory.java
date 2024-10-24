package com.example.admin.sdcb.billing_history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorBillingHistory {
    private String caseCode;
    private String requestId;
    private String transactionId;
    private String statusCode;
    private String totAmt;
    private String eventTime;

    public static ErrorBillingHistory of(BillingHistory billingHistory) {
        return ErrorBillingHistory.builder()
                .caseCode("CASE" + billingHistory.getCaseCode())
                .requestId(billingHistory.getRequestId())
                .transactionId(billingHistory.getTransactionId())
                .statusCode(transStatusCode(billingHistory.getStatusCode()))
                .totAmt(transTotAmt(billingHistory.getTotAmt()))
                .eventTime(billingHistory.getEventTime())
                .build();
    }

    private static String transStatusCode(String statusCode) {
        return switch (statusCode) {
            case "REFUND" -> "환불";
            case "CHARGE/REFUND" -> "결제/취소";
            default -> "결제";
        };
    }

    private static String transTotAmt(String totAmt) {
        String[] totAmtArray = totAmt.split("/");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < totAmtArray.length; i++) {
            sb.append(Math.floor(Math.round(Double.parseDouble(totAmtArray[i]))));
            if (i != totAmtArray.length - 1) {
                sb.append("/");
            }
        }
        return sb.toString();
    }
}
