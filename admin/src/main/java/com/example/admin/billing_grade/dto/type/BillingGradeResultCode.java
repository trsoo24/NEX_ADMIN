package com.example.admin.billing_grade.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BillingGradeResultCode {
    SUCCESS("20000000", "OK")
    ,SERVICE_ERROR("40000001", "내부 오류가 발생하였습니다")
    ,SERVICE_DB_CONNECT_ERROR("40000008","내부 DB 연결 오류입니다.")
    ,SERVICE_DB_ERROR("40000009","내부 DB 연동 오류입니다")
    ,SERVICE_DB_INVALID_ERROR("48888888","알수 없는 DB 오류")
    ,SERVICE_INVALID_ERROR("49999999", "정의되지 않은 기타 오류입니다.")
    ,SERVER_INVALID_ERROR("51999999", "기타");

    private final String value;
    private final String logMsg;
}
