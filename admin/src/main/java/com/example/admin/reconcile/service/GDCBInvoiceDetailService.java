package com.example.admin.reconcile.service;

import com.example.admin.reconcile.dto.GDCBDetailCompare;
import com.example.admin.reconcile.dto.GDCBMonthlyInvoiceSum;
import com.example.admin.reconcile.dto.GoogleMonthlyInvoiceSum;
import com.example.admin.reconcile.mapper.ReconcileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GDCBInvoiceDetailService {
    private final ReconcileMapper reconcileMapper;

    private final String[] revsCategoryArray = {"APP", "APP_SUBSCRIPTION", "CONTENT", "NA", "SPECIAL_APP"};
    private final String[] paymentTypeArray = {"Invoice Details(DCB + 소액결제 + 기타)", "Invoice Details(DCB)", "Invoice Details(소액결제)", "Invoice Details(기타)"};

    public Map<String, List<GDCBDetailCompare>> getGDCBInvoiceDetailMap(String month) {
        Map<String, Object> requestMap = new HashMap<>();
        String[] monthArray = month.split("-");
        List<GDCBDetailCompare> responseList = new ArrayList<>();
        Map<String, List<GDCBDetailCompare>> responseMap = new LinkedHashMap<>();

        // 이번 달 값
        requestMap.put("year", monthArray[0]);
        requestMap.put("month", monthArray[1]);

        gdcbMonthlyInvoiceSumToGDCBCompareDtoList(month, paymentTypeArray[0], getDetails("전체", requestMap), responseList);
        gdcbMonthlyInvoiceSumToGDCBCompareDtoList(month, paymentTypeArray[1], getDetails("00", requestMap), responseList);
        gdcbMonthlyInvoiceSumToGDCBCompareDtoList(month, paymentTypeArray[2], getDetails("PG", requestMap), responseList);
        gdcbMonthlyInvoiceSumToGDCBCompareDtoList(month, paymentTypeArray[3], getDetails("99", requestMap), responseList);

        responseMap.put("invoiceDetailsFileContents", responseList);

        // Google Summary File
        List<GoogleMonthlyInvoiceSum> googleMonthlySumList = getGoogleMonthlySum(requestMap);
        googleMonthlySumToGDCBCompareDtoList(month, googleMonthlySumList, responseMap);

        return responseMap;
    }

    private Map<String, List<GDCBMonthlyInvoiceSum>> getDetails(String paymentType, Map<String, Object> requestMap) {
        Map<String, List<GDCBMonthlyInvoiceSum>> responseMap = new LinkedHashMap<>();
        List<GDCBMonthlyInvoiceSum> monthlyInvoiceBuySumList;
        List<GDCBMonthlyInvoiceSum> monthlyInvoiceRefundSumList;

        if(!paymentType.equals("전체")) {
            requestMap.put("paymentType", paymentType);
            monthlyInvoiceBuySumList = reconcileMapper.selectBuyInvoice(requestMap);
            monthlyInvoiceRefundSumList = reconcileMapper.selectRefundInvoice(requestMap);

        } else {
            monthlyInvoiceBuySumList = reconcileMapper.selectBuyInvoice(requestMap);
            monthlyInvoiceRefundSumList = reconcileMapper.selectRefundInvoice(requestMap);
        }

        addMap(responseMap, monthlyInvoiceBuySumList, monthlyInvoiceRefundSumList);

        return responseMap;
    }

    private void addMap(Map<String, List<GDCBMonthlyInvoiceSum>> map, List<GDCBMonthlyInvoiceSum> buyList, List<GDCBMonthlyInvoiceSum> refundList) {
        setListSize(buyList);
        setListSize(refundList);
        List<GDCBMonthlyInvoiceSum> totalList = new ArrayList<>();

        for (int i = 0; i < buyList.size(); i++) {
            buyList.get(i).calculateChargeSum();
            refundList.get(i).calculateChargeSum();

            GDCBMonthlyInvoiceSum totalSum = GDCBMonthlyInvoiceSum.toTransactionTypeTotal(buyList.get(i), refundList.get(i));
            totalList.add(totalSum);
        }
        addRevsCategoryTotal(totalList);
        addRevsCategoryTotal(buyList);
        addRevsCategoryTotal(refundList);

        map.put("SUM", totalList);
        map.put("CHARGE", buyList);
        map.put("REFUND", refundList);
    }

    private void setListSize(List<GDCBMonthlyInvoiceSum> sumList) {
        // 비어있는 revsCategory 값 채워서 buy, refund list 길이 통일
        boolean[] revsCategoryCheck = new boolean[revsCategoryArray.length];

        for (GDCBMonthlyInvoiceSum sum : sumList) {
            for (int i = 0; i < revsCategoryArray.length; i++) {
                if (sum.getRevsCategory().equals(revsCategoryArray[i])) {
                    revsCategoryCheck[i] = true;
                }
            }
        }

        for (int j = 0; j < revsCategoryCheck.length; j++) {
            if (!revsCategoryCheck[j]) {
                GDCBMonthlyInvoiceSum defaultInvoiceSum = GDCBMonthlyInvoiceSum.generateDefault(sumList.get(0), revsCategoryArray[j]);
                sumList.add(defaultInvoiceSum);
            }
        }

        sumList.sort(Comparator.comparing(GDCBMonthlyInvoiceSum::getRevsCategory));
    }

    private void addRevsCategoryTotal(List<GDCBMonthlyInvoiceSum> sumList) {
        GDCBMonthlyInvoiceSum revsCategoryTotal = GDCBMonthlyInvoiceSum.toRevsCategoryTotal(sumList.get(0));

        for (GDCBMonthlyInvoiceSum sum : sumList) {
            revsCategoryTotal.addTransactionCnt(sum.getTransactionCnt());
            revsCategoryTotal.addAmountSum(sum.getTotalAmountSum());
            revsCategoryTotal.addCurrencySum(sum.getRevsInInvoicedCurrencySum());
        }
        revsCategoryTotal.calculateChargeSum();

        sumList.add(0, revsCategoryTotal);
    }

    private void gdcbMonthlyInvoiceSumToGDCBCompareDtoList(String month, String parameterType, Map<String, List<GDCBMonthlyInvoiceSum>> sumMap, List<GDCBDetailCompare> responseList) { // 객체 하나로 처리
        for (List<GDCBMonthlyInvoiceSum> list : sumMap.values()) {
            for (GDCBMonthlyInvoiceSum sum : list) {
                responseList.add(GDCBDetailCompare.fromTbMonthlyInvoiceSum(month, parameterType, sum));
            }
        }
    }

    private void googleMonthlySumToGDCBCompareDtoList(String month, List<GoogleMonthlyInvoiceSum> googleList, Map<String, List<GDCBDetailCompare>> responseMap) {
        List<GDCBDetailCompare> responseList = new ArrayList<>();
        for (GoogleMonthlyInvoiceSum sum : googleList) {
            responseList.add(GDCBDetailCompare.fromGMonthlyInvoiceSum(month, sum));
        }

        responseMap.put("summaryFileContents", responseList);
    }

    private List<GoogleMonthlyInvoiceSum> getGoogleMonthlySum(Map<String, Object> requestMap) {
        List<GoogleMonthlyInvoiceSum> sumList = reconcileMapper.selectGoogleSummary(requestMap);
        GoogleMonthlyInvoiceSum total = GoogleMonthlyInvoiceSum.toRevsCategoryTotal(sumList.get(0).getYear(), sumList.get(0).getMonth());

        for (GoogleMonthlyInvoiceSum sum : sumList) {
            total.addProductPriceSum(sum.getProductPriceSum());
            total.addTaxSum(sum.getTaxSum());
            total.addTotalAmountSum(sum.getTotalAmountSum());
            total.addRevShareSum(sum.getRevShareSum());
        }
        sumList.add(sumList.size() - 1, total);

        return sumList;
    }
}
