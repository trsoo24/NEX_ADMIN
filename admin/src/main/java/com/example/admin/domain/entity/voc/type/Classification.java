package com.example.admin.domain.entity.voc.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Classification {
    QUESTION("question", "문의"),
    COMPLAINT("complaint", "불만"),
    CHARGE("charge", "결제"),
    REFUND("refund", "환불"),
    CHARGE_DENY("charge_deny", "결제 불가"),
    CHARGE_HISTORY("charge_history", "결제 내역"),
    REQUEST_REFUND("request_refund", "환불 요청"),
    REFUND_PATH("refund_path", "환불 경로")
    ;

    private final String content;
    private final String description;
}
