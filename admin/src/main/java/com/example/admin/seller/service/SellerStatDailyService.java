package com.example.admin.seller.service;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.seller.dto.GetSellerStatDto;
import com.example.admin.seller.dto.SellerDayStat;
import com.example.admin.seller.mapper.SellerStatsDailyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SellerStatDailyService {
    private final SellerStatsDailyMapper sellerStatsDailyMapper;

    public List<SellerDayStat> getSellerStatsDaily(String year, String month, String day) {
        String trxNo = MDC.get("trxNo");

        log.info("[{}] 요청 = {} 일자 판매자 일별 판매 현황 종합", trxNo, year + "-" + month + "-" + day);

        GetSellerStatDto dto = new GetSellerStatDto(year, month, day);

        List<SellerDayStat> sellerDayStatList = sellerStatsDailyMapper.getSellerDayStats(dto);

        log.info("[{}] 응답 = 판매자 일별 판매 현황 {} 건 종합 완료", trxNo, sellerDayStatList.size());

        return sellerDayStatList;
    }
}
