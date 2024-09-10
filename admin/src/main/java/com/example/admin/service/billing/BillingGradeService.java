package com.example.admin.service.billing;

import com.example.admin.domain.dto.billing.BillingGradeDto;
import com.example.admin.domain.entity.billing.BillingGrade;
import com.example.admin.repository.mapper.billinggrade.BillingGradeMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BillingGradeService {
    private final BillingGradeMapper billingGradeMapper;
    private final String[] categories = {"전체", "정상", "연체"};
    private final String[] descriptions = {"청구 대상(명)", "청구 건수(건)", "청구액(원)"};

    public List<BillingGrade> getBillingGrade(String dcb, String yyMm) {
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("dcb", dcb.toUpperCase());
        requestMap.put("yyMm", yyMm);

        return billingGradeMapper.getBillingGrade(requestMap);
    }

    // 등급별 월별 청구 현황 조회
    public List<BillingGradeDto> searchBillingGrade(String dcb, String yyMm) {
        List<BillingGradeDto> billingGradeDtoList = sortList(getBillingGradeList(dcb, yyMm));

        for (BillingGradeDto billingGradeDto : billingGradeDtoList) {
            if (billingGradeDto.getCustGrdCd().equals("A")) {
                billingGradeDto.dtoSetCustGrdCd("전체");
            } else if (billingGradeDto.getCustGrdCd().equals("E")) {
                billingGradeDto.dtoSetCustGrdCd("그 외");
            } else {
                billingGradeDto.dtoSetCustGrdCd(billingGradeDto.getCustGrdCd() + "등급");
            }
        }

        return billingGradeDtoList;
    }

    private List<BillingGradeDto> getBillingGradeList(String dcb, String yyMm) {
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("dcb", dcb.toUpperCase());
        requestMap.put("yyMm", yyMm);
        return billingGradeMapper.getBillingGradeDto(requestMap);
    }

    // 등급별 월 청구 현황 엑셀 생성
    public void exportBillingGradeExcel(String dcb, String yyMm, HttpServletResponse response) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("등급별 청구 현황");
        XSSFRow header = sheet.createRow(0);
        XSSFRow subHeaderRow = sheet.createRow(1);

        header.createCell(0).setCellValue("구분");
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));

        int colIdx = 1;

        for (String category : categories) {
            header.createCell(colIdx).setCellValue(category);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, colIdx, colIdx + 2));
            for (String description : descriptions) {
                subHeaderRow.createCell(colIdx).setCellValue(description);
                colIdx++;
            }
        }

        int rowIdx = 2;
        List<BillingGradeDto> billingGradeDtoList = searchBillingGrade(dcb, yyMm);
        Map<String, BillingGradeDto> billingGradeDtoMap = getBillingGradeMap(billingGradeDtoList);

        for (BillingGradeDto billingGrade : billingGradeDtoMap.values()) {
            XSSFRow row = sheet.createRow(rowIdx++);

            Field[] fields = BillingGradeDto.class.getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true); // private 필드에 접근 가능하게 설정

                try {
                    Object value = fields[i].get(billingGrade);

                    if (value instanceof Integer) {
                        row.createCell(i).setCellValue((Integer) value);
                    } else {
                        row.createCell(i).setCellValue(String.valueOf(value));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=BillingGrade.xlsx");
        response.setStatus(200);
        setCellCenter(workbook);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();
    }

    private void setCellCenter(XSSFWorkbook workbook) { // 셀 가운데 정렬
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
    }

    private Map<String, BillingGradeDto> getBillingGradeMap(List<BillingGradeDto> billingGradeList) {
        Map<String, BillingGradeDto> billingGradeMap = new LinkedHashMap<>();
        List<BillingGradeDto> sortedBillingGradeList = sortList(billingGradeList);

        for (int i = 0; i < sortedBillingGradeList.size(); i++) {
            BillingGradeDto billingGradeDto = sortedBillingGradeList.get(i);

            if (billingGradeDto.getCustGrdCd().equals("A")) {
                billingGradeMap.put("전체", billingGradeDto);
            } else if (billingGradeDto.getCustGrdCd().equals("E")) {
                billingGradeMap.put("그 외", billingGradeDto);
            } else {
                billingGradeMap.put(billingGradeDto.getCustGrdCd() + "등급", billingGradeDto);
            }
        }

        return billingGradeMap;
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
