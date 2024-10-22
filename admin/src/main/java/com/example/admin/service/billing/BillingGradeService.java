package com.example.admin.service.billing;

import com.example.admin.domain.dto.billing.BillingGradeDto;
import com.example.admin.domain.entity.billing.BillingGrade;
import com.example.admin.repository.mapper.billinggrade.BillingGradeMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingGradeService {
    private final BillingGradeMapper billingGradeMapper;
    private final String[] categories = {"전체", "정상", "연체"};
    private final String[] descriptions = {"청구 대상(명)", "청구 건수(건)", "청구액(원)"};

    public Map<String, List<BillingGradeDto>> getBillingGradeList(List<String> dcbs, String yyMm) {
        Map<String, Object> requestMap = new HashMap<>();
        List<BillingGradeDto> totalBillingGradeList = new ArrayList<>();
        Map<String, List<BillingGradeDto>> dcbBillingGradeMap = new LinkedHashMap<>();
        Map<String, List<BillingGradeDto>> responseMap = new LinkedHashMap<>();

        for (String dcb : dcbs) {
            requestMap.put("dcb", dcb.toUpperCase());
            requestMap.put("yyMm", yyMm);

            List<BillingGradeDto> billingGradeList = billingGradeMapper.getBillingGradeDto(requestMap);

            if (dcbs.size() == 1) { // dcb 단일 선택 시
                dcbBillingGradeMap.put("total", billingGradeList);
                return dcbBillingGradeMap;
            }

            dcbBillingGradeMap.put(dcb, billingGradeList);
            totalBillingGradeList.addAll(billingGradeList);
        }

        Map<String, BillingGradeDto> billingGradeMap = new HashMap<>();
        for (BillingGradeDto billingGrade : totalBillingGradeList) {
            String key = billingGrade.getCustGrdCd();

            if (billingGradeMap.containsKey(key)) {
                BillingGradeDto totalBillingGrade = billingGradeMap.get(key);
                totalBillingGrade.addTotalValue(billingGrade);
                billingGradeMap.put(key, totalBillingGrade);
            } else {
                BillingGradeDto totalBillingGrade = BillingGradeDto.toTotalBillingGrade(billingGrade, "total");
                billingGradeMap.put(key, totalBillingGrade);
            }
        }

        responseMap.put("total", new ArrayList<>(billingGradeMap.values()));
        responseMap.putAll(dcbBillingGradeMap);

        return responseMap;
    }


    // 등급별 월별 청구 현황 조회
    public Map<String, List<BillingGradeDto>> searchBillingGrade(List<String> dcbs, String yyMm) {
        Map<String, List<BillingGradeDto>> billingGradeDtoMap = getBillingGradeList(dcbs, yyMm);

        for (List<BillingGradeDto> billingGradeDtoList : billingGradeDtoMap.values()) {
            sortList(billingGradeDtoList);

            for (BillingGradeDto billingGradeDto : billingGradeDtoList) {
                if (billingGradeDto.getCustGrdCd().equals("A")) {
                    billingGradeDto.dtoSetCustGrdCd("전체");
                } else if (billingGradeDto.getCustGrdCd().equals("E")) {
                    billingGradeDto.dtoSetCustGrdCd("그 외");
                } else {
                    billingGradeDto.dtoSetCustGrdCd(billingGradeDto.getCustGrdCd() + "등급");
                }
            }
        }

        return billingGradeDtoMap;
    }


    // 등급별 월 청구 현황 엑셀 생성
    public void exportBillingGradeExcel(List<String> dcbs, String yyMm, HttpServletResponse response) throws IOException {
        log.info("등급별 월 청구 현황 엑셀 API 실행");
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("등급별 청구 현황");
        XSSFCellStyle cellStyle = setCellCenter(workbook);

        Map<String, List<BillingGradeDto>> billingGradeDtoMap = searchBillingGrade(dcbs, yyMm);

        int rowIdx = 0;
        int colIdx = 1;

        for (Map.Entry<String, List<BillingGradeDto>> entry : billingGradeDtoMap.entrySet()) {
            String key = entry.getKey();
            List<BillingGradeDto> billingGradeDtoList = entry.getValue();

            XSSFRow dcbHeader = sheet.createRow(rowIdx);
            XSSFCell dcbCell = dcbHeader.createCell(0);
            dcbCell.setCellValue(key);
            dcbCell.setCellStyle(cellStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx++, 0, entry.getValue().size()));

            XSSFRow categoryHeader = sheet.createRow(rowIdx);
            XSSFRow descriptionHeader = sheet.createRow(rowIdx + 1);
            categoryHeader.createCell(0).setCellValue("구분");
            sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx + 1, 0, 0));

            for (String category : categories) {
                categoryHeader.createCell(colIdx).setCellValue(category);
                sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, colIdx, colIdx + 2));
                for (String description : descriptions) {
                    descriptionHeader.createCell(colIdx).setCellValue(description);
                    colIdx++;
                }
            }
            colIdx = 1;
            rowIdx += 2;

            for (BillingGradeDto billingGradeDto : billingGradeDtoList) {
                XSSFRow row = sheet.createRow(rowIdx++);

                Field[] fields = BillingGradeDto.class.getDeclaredFields();

                for (int i = 0; i < fields.length - 1; i++) { // DCB 값 제외
                    fields[i].setAccessible(true);

                    try {
                        Object value = fields[i].get(billingGradeDto);

                        if (value instanceof Integer) {
                            row.createCell(i).setCellValue((Integer) value);
                        } else {
                            row.createCell(i).setCellValue(String.valueOf(value));
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            rowIdx++;
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=BillingGrade.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();
    }

    private XSSFCellStyle setCellCenter(XSSFWorkbook workbook) { // 셀 가운데 정렬
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        return cellStyle;
    }

    private List<BillingGradeDto> sortList(List<BillingGradeDto> billingGradeList) { // 순서대로 정렬
        List<String> sortList = List.of("A", "1", "2", "3", "4", "5", "6", "7", "E");

        billingGradeList.sort((o1, o2) -> {
            int idx1 = sortList.indexOf(o1.getCustGrdCd());
            int idx2 = sortList.indexOf(o2.getCustGrdCd());
            return Integer.compare(idx1, idx2);
        });

        return billingGradeList;
    }

    @Transactional
    public void insertRandomBillingGradeRecord(String year) {
        String[] dcbArray = {"ADCB", "GDCB", "MDCB", "MSDCB", "NDCB", "PDCB", "SDCB"};
        String[] gradeCd = {"A", "1", "2", "3", "4", "5", "6", "7", "E"};

        for (int i = 1; i < 13; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(year);
            sb.append("-");
            if (i < 10) {
                sb.append("0");
            }
            sb.append(i);

            String statYyMm = sb.toString();

            for (String dcb : dcbArray) {
                for (String grade : gradeCd) {
                    billingGradeMapper.insertBillingGrade(BillingGrade.toBillingGrade(grade, statYyMm, statYyMm, randomCnt(), randomCnt(), randomAmount(),
                            randomCnt(), randomCnt(), randomAmount(), randomCnt(), randomCnt(), randomAmount(), dcb));
                }
            }
        }
    }

    private int randomAmount() {
        Random random = new Random();
        return random.nextInt(1000);
    }

    private int randomCnt() {
        Random random = new Random();
        return random.nextInt();
    }
}
