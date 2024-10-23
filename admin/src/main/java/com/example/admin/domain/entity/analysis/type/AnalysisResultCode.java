package com.example.admin.domain.entity.analysis.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AnalysisResultCode {
    SUCCESS("20000000", "성공"),
    DUPLICATED_SUCCESS("20000001", "성공(기존 요청에 대한 성공)"),
    NO_REFERENCE_LGT("51100070", "LGT 고객 정보 없음"),
    RBP_BLOCK_GRADE6("52000002", "6등급 연체자 결제 불가"),
    RBP_BLOCK_GRADE7("52000003", "7등급 연체자 결제 불가"),
    BLOCK_AUTHORITY("52101200", "직권 차단"),
    DISAGREE_TERMS("51000012", "약관 미동의 고객"),
    NEED_RE_AUTHORIZATION("30000009", "재동의 필요 고객"),
    BLOKC_CAUSE_CHILD("30000008", "법정 대리인 동의 필요 (청소년)"),
    BLOCK_CTN("41000001", "BLOCK CTN"),
    BLOCK_FEE_TYPE("41000002", "BLOCK FEE_TYPE"),
    EXCEED_LIMIT_RBP("52104008", "월 잔여 한도 초과 (RBP)"),
    EXCEED_LIMIT_RCSG("53104008", "월 잔여 한도 초과 (RCSG)"),
    EXPIRED_TOKEN("30000002", "제공된 토큰 만료"),
    OTHER_REASON("other", "기타"),
    ;

    private final String code;
    private final String description;
}
