package com.example.admin.analysis.dto;

import com.example.admin.analysis.dto.type.AnalysisResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiLogs {
    private Date createDt;
    private String seq; // PK
    private String banUnpaidYnCode; // 연체 여부
    private String httpStatus; // 응답 상태 코드
    private String custGrdCd;
    private AnalysisResultCode resultCode; // 서비스 상태 코드
    private String apiResult; // dcb 결과 코드
    private Double amount; // 구매 요청 금액
    private Date reqTime;
    private Date rspTime;
    private String operatorUserToken; // 구매 요청 계정
    private String apiType; //dcb api 코드
    private String seqId; // 로그 단위 unique id
    private String authTransactionId; // google 내부 KEY
    private String merchantName; // 가맹점 이름
    private String itemName; // 상품명
    private String correlationId; // 요청에 대한 고유 ID
    private String ctn; // 요청에 대한 DCB ID
}
