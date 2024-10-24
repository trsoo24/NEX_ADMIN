package com.example.admin.sdcb.billing_history.mapper;

import com.example.admin.sdcb.billing_history.dto.BillingHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BillingHistoryMapper {
    void insertBillingHistory(BillingHistory billingHistory);

    List<BillingHistory> selectBillingHistoryList(Map<String, Object> map);
}
