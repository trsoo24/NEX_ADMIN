package com.example.admin.refund.dto.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Billing {
    // 과금 타입
    private String type;
    // 이벤트 발생 시간
    private String timeStamp;
    // 구매 아이디
    private String correlationId;
    // DCB 측의 고유 아이디
    private String billingAgreementId;
    // 결과 코드
    private String resultCode;
    // 메세지
    private String message;
}
