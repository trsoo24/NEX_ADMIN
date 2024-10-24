package com.example.admin.payment_month.dto;

import com.example.admin.payment_day.dto.DayPaymentDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthPaymentDto {
    private String statMonth;
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

    public static MonthPaymentDto toDto(MonthPayment MonthPayment) {
        return MonthPaymentDto.builder()
                .statMonth(formatDate(MonthPayment.getStat_month()))
                .totalAmount(formatDouble(MonthPayment.getA_stat()))
                .buyAmount(formatDouble(MonthPayment.getB_stat()))
                .buyAmountPercent(MonthPayment.getC_stat())
                .cancelAmount(formatDouble(MonthPayment.getD_stat()))
                .cancelAmountPercent(MonthPayment.getE_stat())
                .refundAmount(formatDouble(MonthPayment.getF_stat()))
                .refundAmountPercent(MonthPayment.getG_stat())
                .paymentCount(formatDouble(MonthPayment.getH_stat()))
                .buyCount(formatDouble(MonthPayment.getI_stat()))
                .buyCountPercent(MonthPayment.getJ_stat())
                .cancelCount(formatDouble(MonthPayment.getK_stat()))
                .cancelCountPercent(MonthPayment.getL_stat())
                .refundCount(formatDouble(MonthPayment.getM_stat()))
                .refundCountPercent(MonthPayment.getN_stat())
                .buyAmountAverage(formatDouble(MonthPayment.getP_stat()))
                .cancelAmountAverage(formatDouble(MonthPayment.getR_stat()))
                .refundAmountAverage(formatDouble(MonthPayment.getT_stat()))
                .build();
    }

    private static String calculatePercent(Double totalAmount, Double amount) {
        return String.format("%.2f%%", Math.round((amount / totalAmount) * 10) / 10.0);
    }

    private static Double calculateAverage(Double amount, Double count) {
        double average = amount / count;
        return Math.round(average * 10) / 10.0;
    }

    private static String formatDate(String statMonth) {
        if (statMonth.equals("TOTAL")) {
            return statMonth;
        } else {
            statMonth = statMonth.substring(statMonth.indexOf("-") + 1) + "월";
            return statMonth.startsWith("0") ? statMonth.substring(1) : statMonth;
        }
    }

    public static MonthPaymentDto toTotalDto(MonthPayment MonthPayment) {
        return MonthPaymentDto.builder()
                .statMonth(formatDate(MonthPayment.getStat_month()))
                .totalAmount(formatDouble(MonthPayment.getA_stat()))
                .buyAmount(formatDouble(MonthPayment.getB_stat()))
                .buyAmountPercent(calculatePercent(MonthPayment.getA_stat(), MonthPayment.getB_stat()))
                .cancelAmount(formatDouble(MonthPayment.getD_stat()))
                .cancelAmountPercent(calculatePercent(MonthPayment.getA_stat(), MonthPayment.getD_stat()))
                .refundAmount(formatDouble(MonthPayment.getF_stat()))
                .refundAmountPercent(calculatePercent(MonthPayment.getA_stat(), MonthPayment.getF_stat()))
                .paymentCount(formatDouble(MonthPayment.getH_stat()))
                .buyCount(formatDouble(MonthPayment.getI_stat()))
                .buyCountPercent(calculatePercent(MonthPayment.getI_stat(), MonthPayment.getH_stat()))
                .cancelCount(formatDouble(MonthPayment.getK_stat()))
                .cancelCountPercent(calculatePercent(MonthPayment.getI_stat(), MonthPayment.getK_stat()))
                .refundCount(formatDouble(MonthPayment.getM_stat()))
                .refundCountPercent(calculatePercent(MonthPayment.getI_stat(), MonthPayment.getM_stat()))
                .buyAmountAverage(formatDouble(calculateAverage(MonthPayment.getB_stat(), MonthPayment.getI_stat())))
                .cancelAmountAverage(formatDouble(calculateAverage(MonthPayment.getD_stat(), MonthPayment.getK_stat())))
                .refundAmountAverage(formatDouble(calculateAverage(MonthPayment.getF_stat(), MonthPayment.getM_stat())))
                .build();
    }

    public static String formatDouble(double value) {
        return DayPaymentDto.formatDouble(value);
    }
}
