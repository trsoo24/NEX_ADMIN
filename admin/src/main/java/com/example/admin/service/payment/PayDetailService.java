package com.example.admin.service.payment;

import com.example.admin.domain.dto.payment.PayDetailDto;
import com.example.admin.domain.dto.payment.field.PayDetailField;
import com.example.admin.repository.mapper.payment.PayDetailMapper;
import com.example.admin.common.service.FunctionUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PayDetailService {
    private final PayDetailMapper payDetailMapper;
    private final FunctionUtil functionUtil;

    public Page<PayDetailDto> getPayDetailPage(String dcb, List<String> selectedPaymentTypes,
                                               String startDate, String endDate,
                                               String searchType, String keyword,
                                               int page, int pageSize) {
        return functionUtil.toPage(getPayDetailList(dcb, selectedPaymentTypes, startDate, endDate, searchType, keyword), page, pageSize);
    }

    public void exportExcel(String dcb, List<String> selectedPaymentTypes,
                            String startDate, String endDate,
                            String searchType, String keyword,
                            HttpServletResponse response) throws IllegalAccessException, IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("건별 상세 이력 조회");
        XSSFRow headerRow = sheet.createRow(1);

        PayDetailField[] payDetailFields = PayDetailField.values();
        Field[] payDetailDtoFields = PayDetailDto.class.getDeclaredFields();

        for (int i = 0; i < payDetailFields.length; i++) {
            headerRow.createCell(i).setCellValue(payDetailFields[i].getDescription());
        }

        int rowIdx = 2;
        List<PayDetailDto> payDetailDtoList = getPayDetailList(dcb, selectedPaymentTypes, startDate, endDate, searchType, keyword);

        for (int j = 0; j < payDetailDtoList.size(); j++) {
            PayDetailDto dto = payDetailDtoList.get(j);
            XSSFRow row = sheet.createRow(rowIdx++);

            for (int k = 0; k < payDetailFields.length; k++) {
                Field field = payDetailDtoFields[k];
                field.setAccessible(true);
                Object value = field.get(dto);

                if (value != null) {
                    row.createCell(k).setCellValue(value.toString());
                } else {
                    row.createCell(k).setCellValue("");
                }
            }
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=PayDetailService.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();
    }

    private List<PayDetailDto> getPayDetailList(String dcb, List<String> selectedPaymentTypes,
                                               String startDate, String endDate,
                                               String searchType, String keyword) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("dcb", dcb);
        requestMap.put("startDate", startDate);
        requestMap.put("endDate", endDate);
        requestMap.put("keyword", keyword);
        requestMap.put("paymentTypes", selectedPaymentTypes != null ? selectedPaymentTypes : new ArrayList<>());

        return filterSearchType(requestMap, searchType);
    }

    private List<PayDetailDto> filterSearchType(Map<String, Object> map, String searchType) {
        return switch (searchType) {
            case "상품명" -> payDetailMapper.selectPaymentDetailByProductName(map);
            case "CTN" -> payDetailMapper.selectPaymentDetailByCtn(map);
            case "company" -> payDetailMapper.selectPaymentDetailByCompanyName(map);
            default -> new ArrayList<>();
        };
    }
}
