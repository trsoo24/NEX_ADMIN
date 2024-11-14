package com.example.admin.reconcile.mapper;

import com.example.admin.reconcile.dto.GDCBMonthlyInvoiceSum;
import com.example.admin.reconcile.dto.InsertReconcileDto;
import com.example.admin.reconcile.dto.GoogleMonthlyInvoiceSum;
import com.example.admin.reconcile.dto.MonthlyInvoiceSum;
import com.example.admin.reconcile.dto.Reconcile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReconcileMapper {
    List<Reconcile> getGDCBReconcileList(Map<String, Object> map);
    String getGDCBReconcileFile(Map<String, Object> map);
    List<GDCBMonthlyInvoiceSum> selectBuyInvoice(Map<String, Object> map);
    List<GDCBMonthlyInvoiceSum> selectRefundInvoice(Map<String, Object> map);
    List<GoogleMonthlyInvoiceSum> selectGoogleSummary(Map<String, Object> map);
}
