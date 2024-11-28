package com.example.admin.range_month.service;

import com.example.admin.range_month.dto.RangeMonth;
import com.example.admin.range_month.mapper.RangeMonthMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RangeMonthService {
    private final RangeMonthMapper rangeMonthMapper;

    public List<RangeMonth> getRangeMonth() {
        Map<String, Object> requestMap = new HashMap<>();
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MONTH, -1);

        // 이전 달의 1일
        cal.set(Calendar.DAY_OF_MONTH, 1);
        String startDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

        // 이전 달의 말일
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

        requestMap.put("startDate", startDate);
        requestMap.put("endDate", endDate);

        List<RangeMonth> rangeMonthList = rangeMonthMapper.getRangeMonthScheduleList(requestMap);

        sortRangeMonthList(rangeMonthList);

        return rangeMonthList;
    }

    private void sortRangeMonthList(List<RangeMonth> rangeMonthList) {
        rangeMonthList.sort(Comparator
                .comparing(RangeMonth::getStat_month) // stat_month 로 정렬
                .thenComparingInt(rangeMonth -> {
                    switch (rangeMonth.getA_stat()) {
                        case "A": return 0;
                        case "1": return 1;
                        case "2": return 2;
                        case "3": return 3;
                        case "4": return 4;
                        case "5": return 5;
                        case "O": return 6;
                        default: return Integer.MAX_VALUE; // 예상치 못한 값에 대한 처리
                    }
                })
        );
    }
}
