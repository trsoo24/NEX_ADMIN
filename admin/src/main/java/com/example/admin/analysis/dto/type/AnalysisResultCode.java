package com.example.admin.analysis.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AnalysisResultCode {
    SUCCESS("20000000", "성공"),
    DUPLICATED_SUCCESS("20000001", "성공(기존 요청에 대한 성공)"),
    DUPLICATE_CHARGE_REQUEST("30000001", "청구 API 중복 요청"),
    EXPIRED_TOKEN("30000002", "제곧된 토큰 만료"),
    DUPLICATE_REFUND_REQUEST("30000003", "환불 API 중복 요청"),
    UNKNOWN_PAYMENT_STATUS("30000004", "Unknown Payment Status"),
    EXPIRED_REFUND("30000006", "환불 기간 만료"),
    OVER_REFUND_VALUE("30000007", "환불 요청 금액 초과"),
    BLOCK_CAUSE_CHILD("30000008", "법정 대리인 동의 필요 (청소년)"),
    NEED_RE_AUTHORIZATION("30000009", "재동의 필요 고객"),
    INCORRECT_REQUEST_URL("30100001", "요청 URL 전문 형식 불일치"),
    INVALID_PARAMETER_URL("30100002", "요청 URL 필수 파라미터 값 오류"),
    INCORRECT_REQUEST_HEADER("30200001", "요청 HEADER 전문 형식 불일치"),
    INVALID_PARAMETER_HEADER("30200002", "요청 HEADER 필수 파라미터 값 오류"),
    INCORRECT_REQUEST_BODY("30300001", "요청 BODY 전문 형식 불일치"),
    INVALID_PARAMETER_BODY("30300002", "요청 BODY 필수 파라미터 값 오류"),
    BLOCK_CTN("41000001", "BLOCK CTN"),
    BLOCK_FEE_TYPE("41000002", "BLOCK FEE_TYPE"),
    DISAGREE_TERMS("51000012", "약관 미동의 고객"),
    NO_REFERENCE_LGT("51100070", "LGT 고객 정보 없음"),
    RBP_BLOCK_GRADE6("52000002", "6등급 연체자 결제 불가"),
    RBP_BLOCK_GRADE7("52000003", "7등급 연체자 결제 불가"),
    BLOCK_AUTHORITY("52101200", "직권 차단"),
    EXCEED_LIMIT_RBP("52104008", "월 잔여 한도 초과 (RBP)"),
    EXCEED_LIMIT_RCSG("53104008", "월 잔여 한도 초과 (RCSG)"),
    ;

    private final String code;
    private final String description;
}
