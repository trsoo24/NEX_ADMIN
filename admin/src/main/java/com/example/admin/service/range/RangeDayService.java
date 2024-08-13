package com.example.admin.service.range;

import com.example.admin.domain.dto.range.RangeDayDto;
import com.example.admin.domain.entity.range.RangeDay;
import com.example.admin.domain.entity.range.RangeDay;
import com.example.admin.repository.mapper.range.RangeDayMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RangeDayService {
    private final RangeDayMapper rangeDayMapper;
    private final String[] A_STAT_ARRAY = {"1", "2", "3", "4", "5", "O"};
    private final Double[] AVERAGE_VALUE_ARRAY = {100000.0, 200000.0, 300000.0, 400000.0, 500000.0, 810000.0};

    public Map<String, List<RangeDayDto>> getRangeDayList(String startDate, String endDate, String dcb) throws IllegalAccessException {
        Map<String, String> paramMap = new HashMap<>(); // 요청 쿼리
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);

        List<RangeDay> rangeDayList = rangeDayMapper.getRangeDayList(paramMap);
        Map<String ,List<RangeDayDto>> responseMap = new LinkedHashMap<>();

        for (RangeDay rangeDay : rangeDayList) {
            List<RangeDayDto> dtoList = new ArrayList<>();
            if (responseMap.containsKey(rangeDay.getStatDay())) {
                dtoList = responseMap.get(rangeDay.getStatDay());
            }

            RangeDayDto dto = RangeDayDto.toDto(rangeDay);

            if (dto.getA_stat().equals("전체")) {
                dtoList.add(0, dto);
            } else {
                dtoList.add(dto);
            }
            responseMap.put(rangeDay.getStatDay(), dtoList);
        }
        return responseMap;
    }

    @Transactional
    public void insertRangeDay(String month) { // DB 에 값 채우기
        List<RangeDay> rangeDayList = new ArrayList<>();

        for (int i = 1; i <= 31; i++) {
            StringBuilder sb = new StringBuilder();
            String day = String.valueOf(i);

            if (i < 10) {
                day = "0" + day;
            }

            sb.append(month);
            sb.append("-");
            sb.append(day);

            RangeDay totalRangeDay = RangeDay.setDefault(sb.toString(), "A");
            rangeDayList.add(totalRangeDay);

            for (String aStat : A_STAT_ARRAY) {
                rangeDayList.add(RangeDay.setDefault(sb.toString(), aStat));
            }

            createRangeDay(sb.toString(), rangeDayList, totalRangeDay);
        }

        for (RangeDay rangeDay : rangeDayList) {
            rangeDay.calculateFStat();
            rangeDay.calculateGStat();
            rangeDay.roundDFG();

            rangeDayMapper.insertRangeDay(rangeDay);
        }
    }

    private void createRangeDay(String day, List<RangeDay> rangeDayList, RangeDay totalRangeDay) {
        double randomValue = 0;

        for (int i = 0; i < 1000000; i++) {
            randomValue = generateRandomValue();

            RangeDay rangeDay = findRangeDay(day, findAStatRange(randomValue), rangeDayList);
            rangeDay.addValue(randomValue);
            totalRangeDay.addValue(randomValue);
        }
    }

    private double generateRandomValue() {
        Random random = new Random();
        return 0 + (800000.0 - 0.0) * random.nextDouble();
    }

    private String findAStatRange(double value) {
        for (int i = 0; i < AVERAGE_VALUE_ARRAY.length; i++) {
            if (value <= AVERAGE_VALUE_ARRAY[i]) {
                return A_STAT_ARRAY[i];
            }
        }
        return null;
    }

    private RangeDay findRangeDay(String date, String aStat, List<RangeDay> rangeDayList) {
        for (RangeDay rangeDay : rangeDayList) {
            if (rangeDay.getStatDay().equals(date) && rangeDay.getAStat().equals(aStat)) {
                return rangeDay;
            }
        }
        return null;
    }
}
