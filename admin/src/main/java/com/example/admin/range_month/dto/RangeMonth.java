package com.example.admin.range_month.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RangeMonth {
    private String stat_month; // 기간
    private String a_stat; // 결제 구간 A , 1 ~ 5 , O ( Over )
    private Double b_stat; // 결제자 수
    private Double c_stat; // 전체 결제건 수
    private Double d_stat; // 결제 금액 ( 원 )
    private Double e_stat; // 취소 금액 ( 원 )
    private Double f_stat; // 실 결제 금액 ( 원 )
    private Double g_stat; // 인당 결제 금액 ( 원 )
    private final String dcb = "GDCB";
}
