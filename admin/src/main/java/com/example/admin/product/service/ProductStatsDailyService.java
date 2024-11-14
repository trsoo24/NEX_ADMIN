package com.example.admin.product.service;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.product.dto.ProductStatsDaily;
import com.example.admin.product.mapper.ProductStatsDailyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductStatsDailyService {
    private final ProductStatsDailyMapper productStatsDailyMapper;

    public List<ProductStatsDaily> getProductStatsDailyList() {
        Map<String, Object> requestMap = new HashMap<>();
        String year = FunctionUtil.yearOfYesterday();
        String month = FunctionUtil.monthOfYesterday();
        String day = FunctionUtil.yesterday();

        requestMap.put("year", year);
        requestMap.put("month", month);
        requestMap.put("day", day);

        return productStatsDailyMapper.getProductDayStats(requestMap);
    }
}
