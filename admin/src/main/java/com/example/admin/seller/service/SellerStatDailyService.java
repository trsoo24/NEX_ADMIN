package com.example.admin.seller.service;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.seller.dto.SellerDayStat;
import com.example.admin.seller.mapper.SellerStatsDailyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
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

        log.info("year : {}, month : {}, day : {}", year, month, day);
        return sellerStatsDailyMapper.getSellerDayStats(requestMap);
    }
}
