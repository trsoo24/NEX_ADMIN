package com.example.admin.service.reconcile.gdcb;

import com.example.admin.domain.dto.reconcile.gdcb.GDCBMonthlyInvoiceSum;
import com.example.admin.repository.mapper.reconcile.gdcb.ReconcileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GDCBInvoiceDetailService {
    private final ReconcileMapper reconcileMapper;

    public Map<String, List<GDCBMonthlyInvoiceSum>> getGDCBInvoiceDetailList(String dcb, String month) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("dcb", dcb);
        String[] monthArray = month.split("-");
        requestMap.put("year", monthArray[0]);
        requestMap.put("month", monthArray[1]);




        Map<String, List<GDCBMonthlyInvoiceSum>> responseMap = new HashMap<>();
        responseMap.put("Invoice Details(DCB + 소액결제 + 기타)", getDetails("전체", requestMap));
        responseMap.put("Invoice Details(DCB)", getDetails("00", requestMap));
        responseMap.put("Invoice Details(소액결제)", getDetails("PG", requestMap));
        responseMap.put("Invoice Details(기타)", getDetails("99", requestMap));

        return responseMap;
    }

    private List<GDCBMonthlyInvoiceSum> getDetails(String paymentType, Map<String, Object> requestMap) {
        List<GDCBMonthlyInvoiceSum> monthlyInvoiceBuySumList = new ArrayList<>();
        List<GDCBMonthlyInvoiceSum> monthlyInvoiceRefundSumList = new ArrayList<>();

        if(!paymentType.equals("전체")) {
            requestMap.put("paymentType", paymentType);
            monthlyInvoiceBuySumList = reconcileMapper.selectBuyInvoice(requestMap);
            monthlyInvoiceRefundSumList = reconcileMapper.selectRefundInvoice(requestMap);
        } else {
            monthlyInvoiceBuySumList = reconcileMapper.selectBuyInvoice(requestMap);
            monthlyInvoiceRefundSumList = reconcileMapper.selectRefundInvoice(requestMap);
        }

        return null;
    }
}
