package com.example.admin.seller.service;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.seller.dto.GetSellerStatDto;
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
        String year = FunctionUtil.yearOfYesterday();
        String month = FunctionUtil.monthOfYesterday();
        String day = FunctionUtil.yesterday();

        GetSellerStatDto dto = new GetSellerStatDto(year, month, day);

        return sellerStatsDailyMapper.getSellerDayStats(dto);
    }
}
