package com.example.admin.repository.mapper.reconcile.gdcb;

import com.example.admin.domain.dto.reconcile.gdcb.GDCBMonthlyInvoiceSum;
import com.example.admin.domain.dto.reconcile.gdcb.InsertReconcileDto;
import com.example.admin.domain.entity.reconcile.gdcb.GoogleMonthlyInvoiceSum;
import com.example.admin.domain.entity.reconcile.gdcb.MonthlyInvoiceSum;
import com.example.admin.domain.entity.reconcile.gdcb.Reconcile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReconcileMapper {
    void insertGDCBReconcile(InsertReconcileDto insertReconcileDto);
    void insertGDCBMonthlyInvoice(MonthlyInvoiceSum monthlyInvoiceSum);
    void insertGoogleMonthlyInvoice(GoogleMonthlyInvoiceSum googleMonthlyInvoiceSum);
    List<Reconcile> getGDCBReconcileList(Map<String, Object> map);
    String getGDCBReconcileFile(Map<String, Object> map);
    List<GDCBMonthlyInvoiceSum> selectBuyInvoice(Map<String, Object> map);
    List<GDCBMonthlyInvoiceSum> selectRefundInvoice(Map<String, Object> map);
    List<GoogleMonthlyInvoiceSum> selectGoogleSummary(Map<String, Object> map);
}
