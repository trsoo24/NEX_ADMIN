package com.example.admin.analysis.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AnalysisResultCode {
    SUCCESS("20000000", "성공"),
    DUPLICATED_SUCCESS("20000001", "성공(기존 요청에 대한 성공)"),
    INVALID_REQUEST("30100001", "요청 URL 형식 불일치"),
    INVALID_REQUEST_PARAMETER("30100002", "요청 URL 필수 파라미터 오류"),
    INVALID_HEADER("30200001", "요청 HEADER 형식 불일치"),
    INVALID_HEADER_PARAMETER("30200002", "요청 HEADER 필수 파라미터 오류"),
    INVALID_BODY("30300001", "요청 BODY 형식 불일치"),
    INVALID_BODY_PARAMETER("30300002", "요청 BODY 필수 파라미터 오류"),
    DUPLICATE_CHARGE("30000001", "청구 API 중복 요청"),
    NO_TRANSACTION_HISTORY("30000002", "거래 내역 찾을 수 없음"),
    DUPLICATE_REFUND("30000003", "환불 API 중복 요청"),
    UNKNOWN_STATUS("30000004", "PaymentStatus: Unknown status"),
    EXPIRED_TO_REFUND("30000006", "환불 기간 만료"),
    OVER_REFUND_VALUE("30000007", "환불 금액이 남은 금액보다 많음"),
    ALREADY_REFUND("30000008", "이미 완전히 환불됨"),
    FAILED_TRANSACTION_TO_REFUND("30000009", "실패된 거래로 환불 요청이 들어옴"),
    EXPIRED_CANCEL_TIME("30000011", "취소 유효 기간 만료"),
    ALREADY_CANCEL("30000012", "이미 취소된 요청"),
    OTHER_SERVICE_ERROR("39999999", "서비스 기타 오류"),
    BLOCK_CTN("41000001", "BLOCK CTN"),
    BLOCK_FEE_TYPE("41000002", "BLOCK FEE_TYPE"),
    DB_ERROR("40000000", "DB_ERROR"),
    DB_CONNECT_ERROR("40000001", "DB_CONNECT_ERROR"),
    DB_INVALID_ERROR("49999999", "DB_INVALID_ERROR"),
    CUSTOMER_DATA_ERROR("51100025", "고객 데이터 오류"),
    NO_CUSTOMER_DATA_OR_NUMBER_CHANGED("51100070", "고객 정보 없거나 번호 이동된 사용자"),
    BLOCK_CORPORATE_PHONE("51000003", "법인 휴대폰 차단"),
    BLOCK_PAUSED_PHONE("51000010", "일시 중지 휴대폰 차단"),
    BLOCK_LOST_PHONE("51000004", "분실 등록 폰 차단"),
    BLOCK_PREPAYMENT_CUSTOMER("51000005", "선불 가입자 차단"),
    BLOCK_NEGATIVE_USER("51000006", "부정 사용자 차단"),
    BLOCK_MINOR_USER("51000007", "만 14세 미만 차단"),
    BLOCK_DUAL_NUMBER("51000008", "듀얼 넘버 차단"),
    BLOCK_MVNO("51000009", "MVNO 차단"),
    DISAGREE_TERMS("51000012", "약관 미동의 고객 차단"),
    BLOCK_OVERDUE("51000013", "연체자 차단"),
    NCAS_READ_TIMEOUT("51777777", "NCAS_READ_TIMEOUT"),
    NCAS_CONNECT_TIMEOUT("51888888", "NCAS_CONNECT_TIMEOUT"),
    NCAS_INVALID_ERROR("51999999", "NCAS_INVALID_ERROR"),
    RBP_RESPONSE_BODY_ERROR("52000001", "RBP 응답 Body 형식 오류"),
    RBP_BLOCK_GRADE6("52000002", "6등급 연체자 결제 불가"),
    RBP_BLOCK_GRADE7("52000003", "7등급 연체자 결제 불가"),
    UNDEFINED_RESULT("52100004", "정의되지 않은 result"),
    UNREGISTERED_MDN("52101001", "등록되지 않은 MDN"),
    SET_TIME_OUT_10("52101005", "내부 처리 중 Time out 발생으로 10초로 설정"),
    LOGIC_ERROR("52101006", "DB 처리 외 내부 로직 상 발생 에러"),
    DB_TRANSACTION_ERROR("52101007", "DB 처리 중 발생 에러"),
    NO_SESSION("52101008", "Session 이 없는 경우"),
    SCP_CONNECTION_FAIL("52101009", "SCB Connection failed"),
    AUTHORITY_BAN("52101200", "직권 차단"),
    OTHER_ERROR("52102000", "기타 에러"),
    NOT_REGISTERED_SOC_CODE("52104000", "SOC-CODE 미 등록"),
    SYSTEM_ERROR("52104100", "System Error"),
    CO_LIMIT_ERROR("52104002", "통합 한도 ERROR"),
    NO_LIMIT_CUSTOMER("52104003", "한도 고객 아님"),
    CUSTOMER_NOT_FOUND("52104004", "존재하지 않는 고객"),
    NOT_ENOUGH_LIMIT("52104008", "잔여 한도 부족"),
    DUPLICATE_REQUEST("52104009", "중복된 Request"),
    NO_INFO_BR_ID("52104010", "해당 BR ID 정보 없음"),
    REQUEST_FOR_COMPLETED("52104011", "완료된 건에 대한 요청"),
    AMOUNT_ERROR("52104015", "금액 오류 ( - 금액 ) 전송"),
    DUPLICATE_RESERVATION("52104018", "중복 예약"),
    DATA_DIFFERENCE_FROM_RESERVATION("52104019", "예약 항목과 다른 차감 Data"),
    DATA_NOT_MATCHED("52104021", "연동 규격에 맞지 않는 데이터 전송"),
    NO_CHARGE_DATA_FOR_CANCEL_REQUEST("52104030", "취소 요청에 대한 결제 이력 없음"),
    CANCEL_AMOUNT_OVER_CHARGE("52104031", "전체 취소 금액이 결제 금액보다 큼"),
    PART_CANCEL_AMOUNT_OVER_TOTAL_CHARGE("52104032", "부분 취소 금액 합이 결제 금액보다 큼"),
    RECEPTION_DATA_ERROR("52104040", "수신 Data 항목 오류"),
    NO_PRICE_FIELD_IN_CMS("52104042", "CMS 타입에서 DBID의 요율이 -1일 때 PRICE 필드 미존재"),
    MULTI_CANCEL_ERROR("52104043", "Multi 건 결제 취소 시 에러"),
    CANCEL_OVER_MONTH("52104044", "이전 월 내역에 대한 결제 취소"),
    RCSG_RESPONSE_TIMEOUT("53888888", "RCSG Response Timeout"),
    INTERNAL_SERVER_ERROR("53999999", "Http Status.INTERNAL_SERVER_ERROR"),
    SYSTEM_ERROR_4100("54004100", "4100-System Error"),
    CUSTOMER_NOT_FOUND_4004("54004004", "4004 존재하지 않는 고객"),
    RECEPTION_DATA_ERROR_4040("54004040", "4040 수신 Data 항목 오류"),
    APIM_TIMEOUT("54008888", "APIM Timeout(Read timeout)"),
    APIM_UNKNOWN_ERROR("54009999", "APIM 알 수 없는 에러가 발생")

    ;

    private final String code;
    private final String description;

    @Override
    public String toString() {
        return code;
    }
}
