package com.example.admin.domain.entity.reconcile.sdcb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BillingHistory { // SK Planet 과 과금 대사 작업 결과
    private String requestId; // 요청 ID
    private String compareMonth; // 비교 대상 월
    private String eventTime; // SKP Event 발생 시간
    private String caseCode; // 오류 유형 1 :  , 2 :  , 3 :
    private String transactionId; // SDCB Transaction ID
    private String statusCode; // 구매 , 취소 상태 코드 ( 거의 CHARGE/REFUND , REFUND )
    private String totAmt; // 총 금액 ( CHARGE / REFUND 는 ChargeValue/RefundValue 로 저장되어있음
    private String ctn; // ctn
    private Integer subNo; // sub no
    private String actType; // 처리 방법 -> 상용에 전부 NULL

    //  dcb, month, case, ctn, page, pageSize
    // month -> startDate , endDate ( yyyy-MM 형식으로 )

    // CASE , 요청 ID , TRANSACTION ID , 결제유형 , 결제금액 , 처리시간 , 사유 , 처리방법 ( 수정 )
}