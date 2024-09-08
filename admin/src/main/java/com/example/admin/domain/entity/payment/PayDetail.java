package com.example.admin.domain.entity.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayDetail { // SDCB 건별 상세 이력 조회
    private String requestId;
    private String transactionId;
    private String partitionId;
    private String paymentType; // Enum ?
    private String status;
    private String ctn;
    private String subscribeNumber; // 고객 가입 번호 (SDCB)
    private String aceNumber; // 고객 계약 번호 (SDCB)
    private String feeType; // 요금제
    private Boolean youngFeeYn; // 청소년 요금제 여부
    private String custTypeCode; // 고객 구분 I : 일반, G : 법인
    private Boolean employeeYn; // 임직원 여부
    private Boolean testPhoneYn; // 테스트폰 여부
    private String purchaseDate; // 구매 날짜
    private String cancelDate;
    private Double price;
    private String cancelCode;
    private String cancelMessage;
    private String productName; // 상품명
    private String productId;
    private String company; // 판매 회사명
    private String merchantName; // 판매자명
    private String createDate;
}
