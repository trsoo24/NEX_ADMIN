package com.example.admin.product.service;


import com.example.admin.common.service.FunctionUtil;
import com.example.admin.product.dto.ProductStatsMonthly;
import com.example.admin.product.mapper.ProductStatsMonthlyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductStatsMonthlyService {
    private final ProductStatsMonthlyMapper productStatsMonthlyMapper;

    public List<ProductStatsMonthly> getProductStatsMonthlyList() {
        Map<String, Object> requestMap = new HashMap<>();
        String year = FunctionUtil.yearOfPreviousMonth();
        String month = FunctionUtil.previousMonth();

        requestMap.put("year", year);
        requestMap.put("month", month);

        return productStatsMonthlyMapper.getProductMonthStats(requestMap);
    }
}
