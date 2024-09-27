package com.example.admin.service.merchant;

import com.example.admin.domain.dto.merchant.BlockMerchantDto;
import com.example.admin.domain.dto.merchant.InsertMerchantInfo;
import com.example.admin.domain.dto.merchant.field.AdmMerchantInfoField;
import com.example.admin.domain.entity.merchant.AdmMerchant;
import com.example.admin.repository.mapper.merchant.AdmMerchantInfoMapper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdmMerchantService {
    private final AdmMerchantInfoMapper admMerchantInfoMapper;
    private final FunctionUtil functionUtil;

    public void insertMerchant(InsertMerchantInfo insertMerchantInfo) {
        if (!existMerchant(insertMerchantInfo.getMerchantNm())) {
            admMerchantInfoMapper.insertAdmMerchant(insertMerchantInfo);
        }
    }

    private boolean existMerchant(String merchantNm) {
        return admMerchantInfoMapper.existMerchant(merchantNm);
    }

    private List<AdmMerchant> searchMerchant(String merchantNm, String startDate, String endDate) {
        Map<String, String> map = new HashMap<>();
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("merchantNm", merchantNm);


        return admMerchantInfoMapper.searchMerchants(map);
    }

    private AdmMerchant searchMerchant(String merchantNm) {
        return admMerchantInfoMapper.selectMerchant(merchantNm);
    }

    private List<AdmMerchant> searchMerchant(String startDate, String endDate) {
        Map<String, String> map = new HashMap<>();
        map.put("startDate", startDate);
        map.put("endDate", endDate);

        return admMerchantInfoMapper.selectAllMerchant(map);
    }

    // 판매자 조회 서비스 로직
    public Page<AdmMerchant> searchMerchantToPage(String dcb, String merchantNm, String startDate, String endDate, int page, int pageSize) {
        List<AdmMerchant> admMerchantList = toMerchantList(dcb, merchantNm, startDate, endDate);

        return functionUtil.toPage(admMerchantList, page, pageSize);
    }

    // 판매자 조회 엑셀 서비스 로직
    public void exportMerchantListExcel(String dcb, String merchantNm, String startDate, String endDate, HttpServletResponse response) throws IllegalAccessException, IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("판매자 목록");
        sheet.createRow(0);
        XSSFRow headerRow = sheet.createRow(1);

        AdmMerchantInfoField[] admMerchantInfoFields = AdmMerchantInfoField.values();
        Field[] adminMerchantFields = AdmMerchant.class.getDeclaredFields();

        for (int i = 0; i < admMerchantInfoFields.length; i++) {
            headerRow.createCell(i).setCellValue(admMerchantInfoFields[i].getDescription());
        }

        int rowIdx = 2;
        List<AdmMerchant> admMerchantList = toMerchantList(dcb, merchantNm, startDate, endDate);

        for (int j = 0; j < admMerchantList.size(); j++) {
            AdmMerchant admMerchant = admMerchantList.get(j);
            XSSFRow row = sheet.createRow(rowIdx++);
            for (int k = 0; k < admMerchantInfoFields.length; k++) {
                Field field = adminMerchantFields[k];
                field.setAccessible(true);
                Object value = field.get(admMerchant);

                if (value != null) {
                    row.createCell(k).setCellValue(value.toString());
                } else {
                    row.createCell(k).setCellValue("");
                }
            }
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=AdminMerchantList.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();
    }

    private List<AdmMerchant> toMerchantList(String dcb, String merchantNm, String startDate, String endDate) {
        if (merchantNm.isEmpty()) {
            return searchMerchant(startDate, endDate);
        } else {
            return searchMerchant(merchantNm, startDate, endDate);
        }
    }

    public void blockMerchant(BlockMerchantDto dto) {
        // TODO Okta 연동 후에 adminId 를 Session & HttpServletRequest 에서 가져오는 로직으로 변경할 것
        String adminId = "test12";

        List<String> merchantNmList = dto.getMerchantNames();

        for (String s : merchantNmList) {
            AdmMerchant admMerchant = searchMerchant(s);
            Map<String, String> requestMap = new HashMap<>();
            requestMap.put("adminId", adminId);
            requestMap.put("dcb", dto.getDcb()); // 아직 활용되지 않음
            requestMap.put("merchantNm", admMerchant.getMerchantNm());

            admMerchantInfoMapper.blockMerchant(requestMap);
        }
    }
}
