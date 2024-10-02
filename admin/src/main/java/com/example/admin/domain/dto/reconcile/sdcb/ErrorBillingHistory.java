package com.example.admin.domain.dto.reconcile.sdcb;

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
}
