package com.example.admin.domain.dto.payment;

import com.example.admin.domain.entity.payment.MonthPayment;
import lombok.*;

import java.text.DecimalFormat;

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
                .statMonth(formatDate(MonthPayment.getStatMonth()))
                .totalAmount(formatDouble(MonthPayment.getAStat()))
                .buyAmount(formatDouble(MonthPayment.getBStat()))
                .buyAmountPercent(MonthPayment.getCStat())
                .cancelAmount(formatDouble(MonthPayment.getDStat()))
                .cancelAmountPercent(MonthPayment.getEStat())
                .refundAmount(formatDouble(MonthPayment.getFStat()))
                .refundAmountPercent(MonthPayment.getGStat())
                .paymentCount(formatDouble(MonthPayment.getHStat()))
                .buyCount(formatDouble(MonthPayment.getIStat()))
                .buyCountPercent(MonthPayment.getJStat())
                .cancelCount(formatDouble(MonthPayment.getKStat()))
                .cancelCountPercent(MonthPayment.getLStat())
                .refundCount(formatDouble(MonthPayment.getMStat()))
                .refundCountPercent(MonthPayment.getNStat())
                .buyAmountAverage(formatDouble(MonthPayment.getPStat()))
                .cancelAmountAverage(formatDouble(MonthPayment.getRStat()))
                .refundAmountAverage(formatDouble(MonthPayment.getTStat()))
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
                .statMonth(formatDate(MonthPayment.getStatMonth()))
                .totalAmount(formatDouble(MonthPayment.getAStat()))
                .buyAmount(formatDouble(MonthPayment.getBStat()))
                .buyAmountPercent(calculatePercent(MonthPayment.getAStat(), MonthPayment.getBStat()))
                .cancelAmount(formatDouble(MonthPayment.getDStat()))
                .cancelAmountPercent(calculatePercent(MonthPayment.getAStat(), MonthPayment.getDStat()))
                .refundAmount(formatDouble(MonthPayment.getFStat()))
                .refundAmountPercent(calculatePercent(MonthPayment.getAStat(), MonthPayment.getFStat()))
                .paymentCount(formatDouble(MonthPayment.getHStat()))
                .buyCount(formatDouble(MonthPayment.getIStat()))
                .buyCountPercent(calculatePercent(MonthPayment.getIStat(), MonthPayment.getHStat()))
                .cancelCount(formatDouble(MonthPayment.getKStat()))
                .cancelCountPercent(calculatePercent(MonthPayment.getIStat(), MonthPayment.getKStat()))
                .refundCount(formatDouble(MonthPayment.getMStat()))
                .refundCountPercent(calculatePercent(MonthPayment.getIStat(), MonthPayment.getMStat()))
                .buyAmountAverage(formatDouble(calculateAverage(MonthPayment.getBStat(), MonthPayment.getIStat())))
                .cancelAmountAverage(formatDouble(calculateAverage(MonthPayment.getDStat(), MonthPayment.getKStat())))
                .refundAmountAverage(formatDouble(calculateAverage(MonthPayment.getFStat(), MonthPayment.getMStat())))
                .build();
    }

    public static String formatDouble(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        return decimalFormat.format(value);
    }
}
