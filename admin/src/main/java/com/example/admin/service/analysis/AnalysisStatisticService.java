package com.example.admin.service.analysis;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.domain.dto.analysis.DayAnalysisDto;
import com.example.admin.domain.entity.analysis.DayAnalysis;
import com.example.admin.domain.entity.analysis.type.AnalysisResultCode;
import com.example.admin.repository.mapper.analysis.AnalysisStatisticsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisStatisticService {
    private final AnalysisStatisticsMapper analysisStatisticsMapper;
    private final FunctionUtil functionUtil;

    private Map<String, Object> putYesterdayDateTime(String day) {
        Map<String, Object> requestMap = new HashMap<>();
//        LocalDate yesterday = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate yesterday = LocalDate.parse(day, formatter);
        String startDate = yesterday.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endDate = yesterday.atTime(23, 59, 59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        requestMap.put("startDate", startDate);
        requestMap.put("endDate", endDate);

        return requestMap;
    }

    // 통계 분석 생성 스케줄러
    public List<DayAnalysis> getAnalysisStatisticsList(String day) {
        Map<String, Object> requestMap = putYesterdayDateTime(day);

        return analysisStatisticsMapper.generateAnalysisStatistics(requestMap);
    }


    /**
     * 여기 아래부터 테스트용
     */
    public Map<String, List<DayAnalysisDto>> getDayAnalysisStatisticsMap(List<String> dcbs, String startDate, String endDate, String codeName) {
        Map<String, List<DayAnalysisDto>> responseMap = new LinkedHashMap<>();

        for (String dcb : dcbs) {
            List<DayAnalysis> dayAnalysisList = getDayAnalysisStatistics(dcb, startDate, endDate, codeName);

            // 카운트 수 내림차순 TOP 12 지정
            List<String> top12List = findTop12FromList(dayAnalysisList);

            // 실패 사유 상위 12개는 코드 설명 : 카운트 수 로 지정 , 아래로는 토탈 모두 더해서 "기타" 로 통합
            List<DayAnalysisDto> responseList = transEntityToDtoList(dayAnalysisList, top12List);

            responseMap.put(dcb, responseList);
        }

        return responseMap;
    }

    private List<DayAnalysis> getDayAnalysisStatistics(String dcb, String startDate, String endDate, String codeName) {
        Map<String, Object>  requestMap = new HashMap<>();
        requestMap.put("dcb", dcb);
        requestMap.put("startDate", startDate);
        requestMap.put("endDate", endDate);
        if(codeName != null) requestMap.put("codeName", codeName);

        return analysisStatisticsMapper.getAnalysisStatisticsList(requestMap);
    }

    public List<DayAnalysisDto> transEntityToDtoList(List<DayAnalysis> dayAnalysisList, List<String> top12List) {
        Map<String, DayAnalysisDto> responseMap = new LinkedHashMap<>();

        for (DayAnalysis dayAnalysis : dayAnalysisList) {
            DayAnalysisDto dayAnalysisDto;

            if (responseMap.containsKey(dayAnalysis.getCreateDt())) {
                dayAnalysisDto = responseMap.get(dayAnalysis.getCreateDt());
            } else {
                dayAnalysisDto = DayAnalysisDto.toDto(dayAnalysis);
            }

            isContainsResultCode(top12List, dayAnalysisDto, dayAnalysis);

            responseMap.put(dayAnalysis.getCreateDt(), dayAnalysisDto);
        }

        List<DayAnalysisDto> responseList = new ArrayList<>(responseMap.values());

        sortDtoMap(responseList, top12List);

        return responseList;
    }

    // 발생 건수가 많은 결과 코드에 포함되는 값
    private void isContainsResultCode(List<String> top12List, DayAnalysisDto dto, DayAnalysis dayAnalysis) {
        if (top12List.contains(dayAnalysis.getResultCode())) {
            dto.addCount(dayAnalysis);
        } else {
            dto.addEtcCount(dayAnalysis.getCodeCount());
        }
    }

    private List<String> findTop12FromList(List<DayAnalysis> dayAnalysisList) {
        Map<String, Integer> countMap = new LinkedHashMap<>(); // 결과 코드별 분류

        for (DayAnalysis dayAnalysis : dayAnalysisList) {
            countMap.put(dayAnalysis.getResultCode(), countMap.getOrDefault(dayAnalysis.getResultCode(), 0) + dayAnalysis.getCodeCount());
        }

        return countMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(12)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private void sortDtoMap(List<DayAnalysisDto> dtoList, List<String> top12List) {
        for (DayAnalysisDto dto : dtoList) {
            // 기타 값 추출
            Integer otherValue = dto.getResultCodeMap().remove("기타");
            Map<String, Integer> resultCodeMap = dto.getResultCodeMap();

            top12List.stream()
                    .filter(resultCodeMap::containsKey)
                    .collect(Collectors.toMap(
                            key -> key,
                            resultCodeMap::get,
                            (e1, e2) -> e1,
                            LinkedHashMap::new));

            // 기타 값 재설정
            dto.addEtcCount(otherValue);
        }
    }

    @Transactional
    public void insertAnalysis(String year, String dcb) {
        for (int j = 1; j <= 12; j++) {
            StringBuilder sb1 = new StringBuilder();
            sb1.append(year);
            String month = String.valueOf(j);

            if (j < 10) {
                month = "0" + month;
            }
            sb1.append(month);
            month = sb1.toString();

            for (int i = 1; i <= 31; i++) {
                StringBuilder sb = new StringBuilder();
                String day = String.valueOf(i);

                if (i < 10) {
                    day = "0" + day;
                }

                sb.append(month);
                sb.append("-");
                sb.append(day);

                dcb = dcb.toUpperCase();
                String date = sb.toString();

                for (AnalysisResultCode code : AnalysisResultCode.values()) {
                    Random random = new Random();
                    String resultCode = code.getCode();
                    int count = random.nextInt(Integer.MAX_VALUE);

                    DayAnalysis analysis = DayAnalysis.to(date, resultCode, count, dcb);

                    analysisStatisticsMapper.insertLogAnalysis(analysis);
                }
            }
        }
    }
}
