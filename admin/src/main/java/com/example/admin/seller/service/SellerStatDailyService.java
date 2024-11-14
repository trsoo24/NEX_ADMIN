package com.example.admin.seller.service;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.seller.dto.SellerDayStat;
import com.example.admin.seller.mapper.SellerStatsDailyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SellerStatDailyService {
    private final SellerStatsDailyMapper sellerStatsDailyMapper;

    public List<SellerDayStat> getSellerStatsDaily() {
        Map<String, Object> requestMap = new HashMap<>();
        String year = FunctionUtil.yearOfYesterday();
        String month = FunctionUtil.monthOfYesterday();
        String day = FunctionUtil.yesterday();

        requestMap.put("year", year);
        requestMap.put("month", month);
        requestMap.put("day", day);

        return sellerStatsDailyMapper.getSellerDayStats(requestMap);
    }
}
