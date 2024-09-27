package com.example.admin.domain.entity.gdcb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthInfo {
    private String authTransactionId;
    private String ctn;
    private Integer version; // 버전
    private String correlationId; // 요청에 대한 고유 ID
    private Long purchaseTime; // 구매 요청 시간
    private String billingAgreement; // 운송인의 청구 계약
    private String operatorUserToken; // 사용자 토큰
    private String paymentDescription; // 지불 내역
    private String merchantName; // 판매자명
    private String itemName; // 상품명
    private String merchantContact; // 판매자 연락처
    private Long itemPrice; // 아이템 가격
    private Long tax; // 세금
    private Long total; // 합계
    private String userLocale; // 언어 코드
    private String dcbTosVersion; // 약관 버전
    private String result; // 결과 코드
    private String cancelNotification;
    private String cancelNotificationDt;
    private String subNo;
    private String aceNo;
    private String ban;
    private String unitMdl;
    private String transactionType;
    private String transactionDt;
    private String transactionResult;
    private String paymentType;
    private String refundDt;
    private String createDt;
    private String feeType;
    private String currency; // 통화 코드
    private int partitionId;

    private String infoLimit ;
    private String infoAvail ;

    private String law1HomeTelNo;
    private String law1PersName;

    private String custGrdCd ;//고객 한도 등급(1~7)
    private String banUnpaid_YnCode ;//연체여부

    private String mpTransactionId ;//소액결제 거래번호
    private String mpTransactionType ;//소액결제 연동 거래 유형(B:구매, R:구매취소)
    private String mpTransactionDt ;//소액결제 거래일자
    private int pYearMonth;
}
