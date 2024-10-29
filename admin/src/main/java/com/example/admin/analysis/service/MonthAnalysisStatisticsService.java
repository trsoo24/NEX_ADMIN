package com.example.admin.analysis.service;

import com.example.admin.analysis.dto.MonthAnalysisDto;
import com.example.admin.analysis.dto.MonthAnalysis;
import com.example.admin.analysis.dto.type.AnalysisResultCode;
import com.example.admin.analysis.mapper.AnalysisStatisticsMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonthAnalysisStatisticsService {
    private final AnalysisStatisticsMapper analysisStatisticsMapper;

    // 통계 분석에 표시될 ResultCode 값에 대한 설명을 Map<에러 코드, 설명> 으로 저장
    private static final Map<String, String> codeToDescriptionMap = Arrays.stream(AnalysisResultCode.values())
            .collect(Collectors.toMap(AnalysisResultCode::getCode, AnalysisResultCode::getDescription));

    @Transactional
    public void insertMonthAnalysis(String date) {
        YearMonth yearMonth = YearMonth.parse(date);

        String startOfLastMonth = yearMonth.atDay(1).toString();
        String endOfLastMonth = yearMonth.atEndOfMonth().toString();

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("startDate", startOfLastMonth);
        requestMap.put("endDate", endOfLastMonth);


        List<MonthAnalysis> monthAnalysisList = analysisStatisticsMapper.generateMonthData(requestMap);

        for (MonthAnalysis monthAnalysis : monthAnalysisList) {
            analysisStatisticsMapper.insertMonthAnalysis(monthAnalysis);
        }
    }

    public Map<String, List<MonthAnalysisDto>> getMonthAnalysisStatisticsMap(List<String> dcbs, String startDate, String endDate, String codeName) {
        Map<String, List<MonthAnalysisDto>> responseMap = new LinkedHashMap<>();

        for (String dcb : dcbs) {
            List<MonthAnalysis> monthAnalysisList = getMonthAnalysisStatistics(dcb, startDate, endDate, codeName);

            // 카운트 수 내림차순 TOP 12 지정
            List<String> top12List = findTop12FromList(monthAnalysisList);

            // 실패 사유 상위 12개는 코드 설명 : 카운트 수 로 지정 , 아래로는 토탈 모두 더해서 "기타" 로 통합
            List<MonthAnalysisDto> responseList = transEntityToDtoList(monthAnalysisList, top12List);

            responseMap.put(dcb, responseList);
        }

        return responseMap;
    }

    private List<MonthAnalysis> getMonthAnalysisStatistics(String dcb, String startDate, String endDate, String codeName) {
        Map<String, Object>  requestMap = new HashMap<>();
        requestMap.put("dcb", dcb);
        requestMap.put("startDate", startDate);
        requestMap.put("endDate", endDate);
        if(codeName != null) requestMap.put("codeName", codeName);

        return analysisStatisticsMapper.getAnalysisMonthStatisticsList(requestMap);
    }

    // DTO 로 변경
    public List<MonthAnalysisDto> transEntityToDtoList(List<MonthAnalysis> monthAnalysisList, List<String> top12List) {
        Map<String, MonthAnalysisDto> responseMap = new LinkedHashMap<>();

        for (MonthAnalysis monthAnalysis : monthAnalysisList) {
            MonthAnalysisDto monthAnalysisDto;

            if (responseMap.containsKey(monthAnalysis.getCreateMonth())) {
                monthAnalysisDto = responseMap.get(monthAnalysis.getCreateMonth());
            } else {
                monthAnalysisDto = MonthAnalysisDto.toDto(monthAnalysis);
            }

            isContainsResultCode(top12List, monthAnalysisDto, monthAnalysis);

            responseMap.put(monthAnalysis.getCreateMonth(), monthAnalysisDto);
        }

        List<MonthAnalysisDto> responseList = new ArrayList<>(responseMap.values());

        sortDtoMap(responseList, top12List);

        return responseList;
    }

    // 발생 건수가 많은 결과 코드에 포함되는 값
    private void isContainsResultCode(List<String> top12List, MonthAnalysisDto dto, MonthAnalysis monthAnalysis) {
        if (top12List.contains(monthAnalysis.getResultCode())) {
            dto.addCount(monthAnalysis);
        } else {
            dto.addEtcCount(monthAnalysis.getCodeCount());
        }
    }

    // 상위 표시될 12개 에러 코드를 찾는다.
    private List<String> findTop12FromList(List<MonthAnalysis> monthAnalysisList) {
        Map<String, Long> countMap = new LinkedHashMap<>(); // 결과 코드별 분류

        for (MonthAnalysis monthAnalysis : monthAnalysisList) {
            countMap.put(monthAnalysis.getResultCode(), countMap.getOrDefault(monthAnalysis.getResultCode(), (long) 0) + monthAnalysis.getCodeCount());
        }

        return countMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(12)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private void sortDtoMap(List<MonthAnalysisDto> dtoList, List<String> top12List) {
        for (MonthAnalysisDto dto : dtoList) {
            // 기타 값 추출 ( 정렬 제외 )
            Long otherValue = dto.getResultCodeMap().remove("기타");
            Map<String, Long> resultCodeMap = dto.getResultCodeMap();

            Map<String, Long> sortedMap = top12List.stream()
                    .filter(resultCodeMap::containsKey) // 상위 12 개 에러 코드 키를 codeToDescriptionMap 의 Map<에러코드, 설명> 에 저장된 설명 값으로 변환
                    .collect(Collectors.toMap(
                            key -> codeToDescriptionMap.getOrDefault(key, key),
                            resultCodeMap::get,
                            (e1, e2) -> e1,
                            LinkedHashMap::new));

            // 정렬된 맵으로 교체
            dto.setResultCodeMap(sortedMap);

            // 기타 값 재설정
            dto.addEtcCount(otherValue);
        }
    }

    public void exportMonthAnalysisStatisticExcel(List<String> dcbs, String startDate, String endDate, String codeName, HttpServletResponse response) throws IOException {
        Map<String, List<MonthAnalysisDto>> monthAnalysisStatisticsMap = getMonthAnalysisStatisticsMap(dcbs, startDate, endDate, codeName);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("월별 통계 분석");
        XSSFCellStyle middle = workbook.createCellStyle();
        middle.setAlignment(HorizontalAlignment.CENTER);

        int rowIdx = 0;
        int colIdx = 1;
        for (Map.Entry<String, List<MonthAnalysisDto>> entry : monthAnalysisStatisticsMap.entrySet()) {
            String key = entry.getKey();
            List<MonthAnalysisDto> dtoList = entry.getValue();

            // Map의 key 병합 처리
            XSSFCell keyCell = sheet.createRow(rowIdx).createCell(0);
            keyCell.setCellValue(key);
            keyCell.setCellStyle(middle); // 중앙 정렬

            sheet.addMergedRegion(new CellRangeAddress(rowIdx++, rowIdx++, 0, dtoList.size()));

            // 상위 실패 사유 리스트
            List<String> topResultCodeList = getTopResultList(dtoList.get(0).getResultCodeMap());

            int rowIdxForResultCode = rowIdx;

            for (String topResultCode : topResultCodeList) {
                String reasonDescription = getAnalysisResultCode(topResultCode);
                sheet.createRow(1 + rowIdx++).createCell(0).setCellValue(reasonDescription);
            }

            int rowIdxForDto = rowIdxForResultCode;

            int colIdxForMonthAnalysisDto = colIdx;
            for (MonthAnalysisDto monthAnalysisDto : dtoList) {
                XSSFRow row = sheet.getRow(rowIdxForDto);

                if (row == null) {
                    row = sheet.createRow(rowIdxForDto);
                }

                rowIdxForDto++;

                row.createCell(colIdxForMonthAnalysisDto).setCellValue(monthAnalysisDto.getCreateMonth());

                for (Long value : monthAnalysisDto.getResultCodeMap().values()) {
                    row = sheet.getRow(rowIdxForDto);

                    if (row == null) {
                        row = sheet.createRow(rowIdxForDto);
                    }

                    rowIdxForDto++;

                    row.createCell(colIdxForMonthAnalysisDto).setCellValue(value);
                }
                colIdxForMonthAnalysisDto++;
                rowIdxForDto = rowIdxForResultCode;
            }
            rowIdx++;
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=MonthAnalysisStatistic.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();
    }

    private List<String> getTopResultList(Map<String, Long> resultCodeMap) {
        List<String> responseList = new ArrayList<>();

        for (Map.Entry<String, Long> entry : resultCodeMap.entrySet()) {
            String key = entry.getKey();

            responseList.add(key);
        }

        return responseList;
    }

    private String getAnalysisResultCode(String resultCode) {
        if(codeToDescriptionMap.containsKey(resultCode)) {
            return codeToDescriptionMap.get(resultCode);
        }
        return "기타";
    }
}
