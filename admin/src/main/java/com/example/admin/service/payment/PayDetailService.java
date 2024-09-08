package com.example.admin.service.payment;

import com.example.admin.domain.dto.payment.PayDetailDto;
import com.example.admin.domain.dto.payment.field.PayDetailField;
import com.example.admin.domain.entity.merchant.AdmMerchant;
import com.example.admin.repository.mapper.payment.PayDetailMapper;
import com.example.admin.service.FunctionUtil;
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
                                               String searchType, List<String> keywords,
                                               int page, int pageSize) {
        return functionUtil.toPage(getPayDetailList(dcb, selectedPaymentTypes, startDate, endDate, searchType, keywords), page, pageSize);
    }

    public void exportExcel(String dcb, List<String> selectedPaymentTypes,
                            String startDate, String endDate,
                            String searchType, List<String> keywords,
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
        List<PayDetailDto> payDetailDtoList = getPayDetailList(dcb, selectedPaymentTypes, startDate, endDate, searchType, keywords);

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

    // TODO DCB 값은 SDCB 로 고정인데 파라미터에 있어야되나?
    private List<PayDetailDto> getPayDetailList(String dcb, List<String> selectedPaymentTypes,
                                               String startDate, String endDate,
                                               String searchType, List<String> keywords) {

        List<PayDetailDto> resultList = new ArrayList<>();

        for (String paymentType : selectedPaymentTypes) {
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("startDate", startDate);
            requestMap.put("endDate", endDate);
            requestMap.put("paymentType", paymentType);
            List<PayDetailDto> payDetailDtoList = filterSearchType(requestMap, searchType, keywords);

            resultList.addAll(payDetailDtoList);
        }

        resultList.sort(Comparator.comparing(PayDetailDto::getPurchaseDate));
        return resultList;
    }

    private List<PayDetailDto> filterSearchType(Map<String, Object> map, String searchType, List<String> keywords) {
        switch (searchType) {
            case "productName" :
                map.put("productName", keywords);
                return payDetailMapper.getPayDetailsByProductName(map);
            case "ctn" :
                map.put("ctn", keywords);
                return payDetailMapper.getPayDetailsByCtn(map);
            case "company" :
                map.put("company", keywords);
                return payDetailMapper.getPayDetailsByCompany(map);

            default: return null;
        }
    }
}
