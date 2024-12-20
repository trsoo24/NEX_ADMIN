package com.example.admin.product.service;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.product.dto.GetProductStatDto;
import com.example.admin.product.dto.ProductStatsDaily;
import com.example.admin.product.mapper.ProductStatsDailyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductStatsDailyService {
    private final ProductStatsDailyMapper productStatsDailyMapper;

    public List<ProductStatsDaily> getProductStatsDailyList(String year, String month, String day) {
        String trxNo = MDC.get("trxNo");

        GetProductStatDto dto = new GetProductStatDto(year, month, day);

        log.info("[{}] 요청 = {} 일자 상품 일별 판매 현황 종합", trxNo, year + "-" + month + "-" + day);

        List<ProductStatsDaily> productStatsDailyList =  productStatsDailyMapper.getProductDayStats(dto);

        log.info("[{}] 응답 = 상품 일별 판매 현황 {} 건 종합 완료", trxNo, productStatsDailyList.size());

        return productStatsDailyList;
    }
}
