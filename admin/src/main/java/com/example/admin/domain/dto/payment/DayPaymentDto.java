package com.example.admin.domain.dto.payment;

import com.example.admin.domain.entity.payment.DayPayment;
import lombok.*;

import java.text.DecimalFormat;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DayPaymentDto {
    private String statDay; // '2024-07-04' 방식 저장
    private String totalAmount; // 총 금액
    private String buyAmount; // 구매 금액
    private String buyAmountPercent; // 구매 금액 비율
    private String cancelAmount; // 취소 금액
    private String cancelAmountPercent; // 취소 금액 비율
    private String refundAmount; // 환불 금액
    private String refundAmountPercent; // 환불 금액 비율
    private String paymentCount; // 총 결제 건 수
    private String buyCount; // 구매 수
    private String buyCountPercent; // 구매 건 수 비율
    private String cancelCount; // 취소 수
    private String cancelCountPercent; // 취소 건 수 비율
    private String refundCount; // 환불 수
    private String refundCountPercent; // 환불 건 수 비율
    private String buyAmountAverage;    // 건별 평균 구매액
    private String cancelAmountAverage; // 건별 평균 취소액
    private String refundAmountAverage; // 건별 평균 환불액

    public static DayPaymentDto toDto(DayPayment dayPayment) {
        return DayPaymentDto.builder()
                .statDay(formatDate(dayPayment.getStat_day()))
                .totalAmount(formatDouble(dayPayment.getA_stat()))
                .buyAmount(formatDouble(dayPayment.getB_stat()))
                .buyAmountPercent(dayPayment.getC_stat())
                .cancelAmount(formatDouble(dayPayment.getD_stat()))
                .cancelAmountPercent(dayPayment.getE_stat())
                .refundAmount(formatDouble(dayPayment.getF_stat()))
                .refundAmountPercent(dayPayment.getG_stat())
                .paymentCount(formatDouble(dayPayment.getH_stat()))
                .buyCount(formatDouble(dayPayment.getI_stat()))
                .buyCountPercent(dayPayment.getJ_stat())
                .cancelCount(formatDouble(dayPayment.getK_stat()))
                .cancelCountPercent(dayPayment.getL_stat())
                .refundCount(formatDouble(dayPayment.getM_stat()))
                .refundCountPercent(dayPayment.getN_stat())
                .buyAmountAverage(formatDouble(dayPayment.getP_stat()))
                .cancelAmountAverage(formatDouble(dayPayment.getR_stat()))
                .refundAmountAverage(formatDouble(dayPayment.getT_stat()))
                .build();
    }

    private static String calculatePercent(Double totalAmount, Double amount) {
        return String.format("%.2f%%", Math.round((amount / totalAmount) * 10) / 10.0);
    }

    private static Double calculateAverage(Double amount, Double count) {
        double average = amount / count;
        return Math.round(average * 10) / 10.0;
    }

    private static String formatDate(String statDay) {
        if (statDay.equals("TOTAL")) {
            return statDay;
        }

        if (statDay.charAt(statDay.length() - 2) == '0') { // 일자가 한 자릿수일 때
            return statDay.substring(statDay.length() - 1) + "일";
        } else {
            return statDay.substring(statDay.length() - 2) + "일";
        }
    }

    public static DayPaymentDto toTotalDto(DayPayment dayPayment) {
        return DayPaymentDto.builder()
                .statDay(formatDate(dayPayment.getStat_day()))
                .totalAmount(formatDouble(dayPayment.getA_stat()))
                .buyAmount(formatDouble(dayPayment.getB_stat()))
                .buyAmountPercent(calculatePercent(dayPayment.getA_stat(), dayPayment.getB_stat()))
                .cancelAmount(formatDouble(dayPayment.getD_stat()))
                .cancelAmountPercent(calculatePercent(dayPayment.getA_stat(), dayPayment.getD_stat()))
                .refundAmount(formatDouble(dayPayment.getF_stat()))
                .refundAmountPercent(calculatePercent(dayPayment.getA_stat(), dayPayment.getF_stat()))
                .paymentCount(formatDouble(dayPayment.getH_stat()))
                .buyCount(formatDouble(dayPayment.getI_stat()))
                .buyCountPercent(calculatePercent(dayPayment.getI_stat(), dayPayment.getH_stat()))
                .cancelCount(formatDouble(dayPayment.getK_stat()))
                .cancelCountPercent(calculatePercent(dayPayment.getI_stat(), dayPayment.getK_stat()))
                .refundCount(formatDouble(dayPayment.getM_stat()))
                .refundCountPercent(calculatePercent(dayPayment.getI_stat(), dayPayment.getM_stat()))
                .buyAmountAverage(formatDouble(calculateAverage(dayPayment.getB_stat(), dayPayment.getI_stat())))
                .cancelAmountAverage(formatDouble(calculateAverage(dayPayment.getD_stat(), dayPayment.getK_stat())))
                .refundAmountAverage(formatDouble(calculateAverage(dayPayment.getF_stat(), dayPayment.getM_stat())))
                .build();
    }

    public static String formatDouble(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        return decimalFormat.format(value);
    }
}
