package com.example.admin.domain.entity.gdcb;

import com.example.admin.domain.entity.refund.ManualRefund;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthInfo {
    private String authTransactionId;
    private int partitionId;
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
    private String infoLimit;
    private String infoAvail;
    private String custGrdCd;//고객 한도 등급(1~7)
    private String banUnpaid_YnCode;//연체여부
    private String mpTransactionId;//소액결제 거래번호
    private String mpTransactionType;//소액결제 연동 거래 유형(B:구매, R:구매취소)
    private String mpTransactionDt;//소액결제 거래일자

    /**
     * 구매 예약
     */
    public static final String AUTH = "A";
    /**
     * 구매 취소
     */
    public static final String CANCEL = "C";
    /**
     * 구매
     */
    public static final String CHARGE = "B";
    /**
     * 환불
     */
    public static final String REFUND = "R";

    public static AuthInfo toAuthInfo(ManualRefund manualRefund) {
        return AuthInfo.builder()
                .correlationId(manualRefund.getCorrelationId())
                .authTransactionId(manualRefund.getAuthTransactionId())
                .ctn(manualRefund.getCtn())
                .itemName(manualRefund.getItemName())
                .merchantName(manualRefund.getMerchantName())
                .paymentType(manualRefund.getPaymentDescription())
                .merchantContact(manualRefund.getMerchantContact())
                .transactionType(manualRefund.getTransactionType())
                .transactionDt(manualRefund.getTransactionDt())
                .total(manualRefund.getTotal())
                .itemPrice(manualRefund.getItemPrice())
                .build();
    }


    public static AuthInfo tloToAuthInfo(String tlo) {
        String[] authString = tlo.split("\\|");

        Function<String, String> extractValue = str -> {
            String[] parts = str.split("=");
            return parts.length > 1 ? parts[1] : "";
        };

        if (authString[0].indexOf("=") > 0) {
            return AuthInfo.builder()
                    .transactionType(extractValue.apply(authString[0]))
                    .correlationId(extractValue.apply(authString[1]))
                    .authTransactionId(extractValue.apply(authString[2]))
                    .ctn(extractValue.apply(authString[3]))
                    .subNo(extractValue.apply(authString[4]))
                    .aceNo(extractValue.apply(authString[5]))
                    .ban(extractValue.apply(authString[6]))
                    .unitMdl(extractValue.apply(authString[7]))
                    .itemName(extractValue.apply(authString[8]))
                    .merchantName(extractValue.apply(authString[9]))
                    .merchantContact(extractValue.apply(authString[10]))
                    .itemPrice(Long.valueOf(extractValue.apply(authString[11])))
                    .tax(Long.valueOf(extractValue.apply(authString[12])))
                    .total(Long.valueOf(extractValue.apply(authString[13])))
                    .createDt(extractValue.apply(authString[14]))
                    .transactionDt(extractValue.apply(authString[15]))
                    .build();
        } else {
            String[] split = authString[0].split(":");
            // Transaction Date
            String tdt = null;
            tdt = authString[14].length() > 1 ? authString[14] : tdt;
            tdt = authString[15].length() > 1 ? authString[15] : tdt;
            tdt = authString[16].length() > 1 ? authString[16] : tdt;

            return AuthInfo.builder()
                    .ctn(split[1])
                    .correlationId(authString[1])
                    .transactionType(authString[2])
                    .authTransactionId(authString[3])
                    .itemPrice(Long.valueOf(authString[4]))
                    .tax(Long.valueOf(authString[5]))
                    .total(Long.valueOf(authString[6]))
                    .itemName(authString[7])
                    .merchantName(authString[8])
                    .merchantContact(authString[9])
                    .currency(authString[10])
                    .unitMdl(authString[11])
                    .feeType((authString[12]))
                    .createDt(authString[13])
                    .aceNo(authString[15])
                    .ban(authString[17])
                    .transactionDt(tdt)
                    .build();
        }
    }
}
