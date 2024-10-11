package com.example.admin.repository.mapper.refund;

import com.example.admin.domain.entity.gdcb.AuthInfo;
import com.example.admin.domain.entity.refund.ManualRefund;
import com.example.admin.domain.entity.refund.ManualRefundFileInfo;
import com.example.admin.domain.entity.refund.vo.LgudcbEaiSdw;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface RefundMapper {
    void insertGdcbAuthInfo(AuthInfo authInfo);
    void insertManualRefund(ManualRefund manualRefund);
    void insertManualRefundFileInfo(ManualRefundFileInfo manualRefundFileInfo);
    void insertEai(LgudcbEaiSdw lgudcbEaiSdw);
    ManualRefund selectManualRefundByCorrelationId(String correlationId);
    void updateRefundAuth(Map<String, Object> map);
    AuthInfo getAuthInfo(Map<String, Object> map);
    ManualRefundFileInfo selectBillingFileInfo(String requestName);
    void updateRefundFileInfo(Map<String, Object> map);
}