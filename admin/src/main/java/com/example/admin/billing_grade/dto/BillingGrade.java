package com.example.admin.billing_grade.dto;

import com.example.admin.billing_grade.dto.type.BillingGradeResultCode;
import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class BillingGrade { // 월별 청구 현황 ( 등급별 ) 조회
    private String custGrdCd; // 고객 등급
    private String statYyMm;
    private String regDt;
    private Integer allAceCnt; // 전체 청구 대상 (명)
    private Integer allCnt; // 전체 청구 건수 (건)
    private Integer allAmount; // 전체 청구액 (원)
    private Integer paidAceCnt; // 정상
    private Integer paidCnt;
    private Integer paidAmount;
    private Integer unpaidAceCnt; // 연체
    private Integer unpaidCnt;
    private Integer unpaidAmount;
    private final String dcb = "GDCB";

    private String resultCode;
    private final String api_type1 = "charge";
    private final String api_type2 = "reversal";
    private final String api_type3 = "refund";
    private final String paid = "N";
    private final String unpaid = "Y";
    private String firstDay;
    private String lastDay;


    public BillingGrade() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        setStatYyMm(new SimpleDateFormat("yyyyMM").format(cal.getTime()));
        setResultCode(BillingGradeResultCode.SUCCESS.getValue());
        setFirstDay(getStatYyMm()+"01000000");
        setLastDay(getStatYyMm()+cal.getActualMaximum(Calendar.DAY_OF_MONTH)+"235959");
    }
}
