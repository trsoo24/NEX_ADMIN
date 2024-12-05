package com.example.admin.seller.service;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.seller.dto.BlockSellerDto;
import com.example.admin.seller.dto.field.AdmSellerInfoField;
import com.example.admin.seller.dto.AdmSeller;
import com.example.admin.seller.mapper.AdmSellerInfoMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdmSellerService {
    private final AdmSellerInfoMapper admSellerInfoMapper;

    public List<AdmSeller> searchSeller(String sellerName, String startDate, String endDate, boolean isExcel) {
        String trxNo = MDC.get("trxNo");

        Map<String, String> map = new HashMap<>();
        map.put("startDate", FunctionUtil.toYYYYmmDD(startDate));
        map.put("endDate", FunctionUtil.toYYYYmmDD(endDate));
        map.put("sellerName", sellerName);

        log.info("[{}] 요청 = {} 부터 {} 까지 판매자 목록 조회", trxNo, startDate, endDate);

        List<AdmSeller> admSellerList = admSellerInfoMapper.searchSellers(map);

        if (!isExcel) {
            log.info("[{}] 응답 = 판매자 목록 {} 건 조회 완료", trxNo, admSellerList.size());
        }

        return admSellerList;
    }

    // 판매자 조회 엑셀 서비스 로직
    public void exportSellerListExcel(String sellerName, String startDate, String endDate, HttpServletResponse response) throws IllegalAccessException, IOException {
        String trxNo = MDC.get("trxNo");

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("판매자 목록");
        sheet.createRow(0);
        XSSFRow headerRow = sheet.createRow(1);

        AdmSellerInfoField[] admSellerInfoFields = AdmSellerInfoField.values();
        Field[] adminSellerFields = AdmSeller.class.getDeclaredFields();

        for (int i = 0; i < admSellerInfoFields.length; i++) {
            headerRow.createCell(i).setCellValue(admSellerInfoFields[i].getDescription());
        }

        int rowIdx = 2;
        List<AdmSeller> admSellerList = searchSeller(sellerName, startDate, endDate, true);

        for (int j = 0; j < admSellerList.size(); j++) {
            AdmSeller admSeller = admSellerList.get(j);
            XSSFRow row = sheet.createRow(rowIdx++);
            for (int k = 0; k < admSellerInfoFields.length; k++) {
                Field field = adminSellerFields[k];
                field.setAccessible(true);
                Object value = field.get(admSeller);

                if (value != null) {
                    row.createCell(k).setCellValue(value.toString());
                } else {
                    row.createCell(k).setCellValue("");
                }
            }
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=AdminSellerList.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();

        log.info("[{}] 응답 = 판매자 목록 엑셀 생성 완료", trxNo);
    }

    public void blockSeller(BlockSellerDto dto) {
        // TODO Okta 연동 후에 adminId 를 Session & HttpServletRequest 에서 가져오는 로직으로 변경할 것
        String trxNo = MDC.get("trxNo");

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("updId", dto.getUpdId());
        requestMap.put("blockId", dto.getBlockId());
        requestMap.put("sellerNames", dto.getSellerNames());

        log.info("[{}] 요청 = 판매자 {} 건 차단 요청", trxNo, dto.getSellerNames().size());

        boolean blockResponse = admSellerInfoMapper.blockSeller(requestMap);

        if (blockResponse) {
            log.info("[{}] 응답 = 판매자 {} 건 차단 완료", trxNo, dto.getSellerNames().size());
        } else {
            log.info("[{}] 응답 = 판매자 차단 실패", trxNo);
        }
    }
}
