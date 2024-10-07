package com.example.admin.repository.mapper.reconcile.sdcb;

import com.example.admin.domain.entity.reconcile.sdcb.BillingHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BillingHistoryMapper {
    void insertBillingHistory(BillingHistory billingHistory);

    List<BillingHistory> selectBillingHistoryList(Map<String, Object> map);
}
