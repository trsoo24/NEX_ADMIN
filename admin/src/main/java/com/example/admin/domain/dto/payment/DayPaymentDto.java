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
                .statDay(formatDate(dayPayment.getStatDay()))
                .totalAmount(formatDouble(dayPayment.getAStat()))
                .buyAmount(formatDouble(dayPayment.getBStat()))
                .buyAmountPercent(dayPayment.getCStat())
                .cancelAmount(formatDouble(dayPayment.getDStat()))
                .cancelAmountPercent(dayPayment.getEStat())
                .refundAmount(formatDouble(dayPayment.getFStat()))
                .refundAmountPercent(dayPayment.getGStat())
                .paymentCount(formatDouble(dayPayment.getHStat()))
                .buyCount(formatDouble(dayPayment.getIStat()))
                .buyCountPercent(dayPayment.getJStat())
                .cancelCount(formatDouble(dayPayment.getKStat()))
                .cancelCountPercent(dayPayment.getLStat())
                .refundCount(formatDouble(dayPayment.getMStat()))
                .refundCountPercent(dayPayment.getNStat())
                .buyAmountAverage(formatDouble(dayPayment.getPStat()))
                .cancelAmountAverage(formatDouble(dayPayment.getRStat()))
                .refundAmountAverage(formatDouble(dayPayment.getTStat()))
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
                .statDay(formatDate(dayPayment.getStatDay()))
                .totalAmount(formatDouble(dayPayment.getAStat()))
                .buyAmount(formatDouble(dayPayment.getBStat()))
                .buyAmountPercent(calculatePercent(dayPayment.getAStat(), dayPayment.getBStat()))
                .cancelAmount(formatDouble(dayPayment.getDStat()))
                .cancelAmountPercent(calculatePercent(dayPayment.getAStat(), dayPayment.getDStat()))
                .refundAmount(formatDouble(dayPayment.getFStat()))
                .refundAmountPercent(calculatePercent(dayPayment.getAStat(), dayPayment.getFStat()))
                .paymentCount(formatDouble(dayPayment.getHStat()))
                .buyCount(formatDouble(dayPayment.getIStat()))
                .buyCountPercent(calculatePercent(dayPayment.getIStat(), dayPayment.getHStat()))
                .cancelCount(formatDouble(dayPayment.getKStat()))
                .cancelCountPercent(calculatePercent(dayPayment.getIStat(), dayPayment.getKStat()))
                .refundCount(formatDouble(dayPayment.getMStat()))
                .refundCountPercent(calculatePercent(dayPayment.getIStat(), dayPayment.getMStat()))
                .buyAmountAverage(formatDouble(calculateAverage(dayPayment.getBStat(), dayPayment.getIStat())))
                .cancelAmountAverage(formatDouble(calculateAverage(dayPayment.getDStat(), dayPayment.getKStat())))
                .refundAmountAverage(formatDouble(calculateAverage(dayPayment.getFStat(), dayPayment.getMStat())))
                .build();
    }

    public static String formatDouble(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        return decimalFormat.format(value);
    }
}
