package com.example.admin.refund.mapper;

import com.example.admin.auth.dto.AuthInfo;
import com.example.admin.refund.dto.ManualRefund;
import com.example.admin.refund.dto.ManualRefundFileInfo;
import com.example.admin.refund.dto.vo.LgudcbEaiSdw;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface RefundMapper {
    boolean insertManualRefund(ManualRefund manualRefund);
    boolean insertManualRefundFileInfo(ManualRefundFileInfo manualRefundFileInfo);
    ManualRefund selectManualRefundByCorrelationId(String correlationId);
    void updateRefundAuth(Map<String, Object> map);
    AuthInfo getAuthInfo(Map<String, Object> map);
    ManualRefundFileInfo selectBillingFileInfo(String requestName);
    void updateRefundFileInfo(Map<String, Object> map);
}