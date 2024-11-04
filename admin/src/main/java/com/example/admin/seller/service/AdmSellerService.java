package com.example.admin.seller.service;

import com.example.admin.seller.dto.BlockSellerDto;
import com.example.admin.seller.dto.InsertSellerInfo;
import com.example.admin.seller.dto.field.AdmSellerInfoField;
import com.example.admin.seller.dto.AdmSeller;
import com.example.admin.seller.mapper.AdmSellerInfoMapper;
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
public class AdmSellerService {
    private final AdmSellerInfoMapper admSellerInfoMapper;
    private final FunctionUtil functionUtil;

    public void insertSeller(InsertSellerInfo insertSellerInfo) {
        if (!existSeller(insertSellerInfo.getSellerName())) {
            admSellerInfoMapper.insertAdmSeller(insertSellerInfo);
        }
    }

    private boolean existSeller(String sellerName) {
        return admSellerInfoMapper.existSeller(sellerName);
    }

    private List<AdmSeller> searchSeller(String sellerName, String startDate, String endDate) {
        Map<String, String> map = new HashMap<>();
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("sellerName", sellerName);


        return admSellerInfoMapper.searchSellers(map);
    }

    private AdmSeller searchSeller(String sellerName) {
        return admSellerInfoMapper.selectSeller(sellerName);
    }

    private List<AdmSeller> searchSeller(String startDate, String endDate) {
        Map<String, String> map = new HashMap<>();
        map.put("startDate", startDate);
        map.put("endDate", endDate);

        return admSellerInfoMapper.selectAllSeller(map);
    }

    // 판매자 조회 서비스 로직
    public Page<AdmSeller> searchSellerToPage(String dcb, String sellerName, String startDate, String endDate, int page, int pageSize) {
        List<AdmSeller> admSellerList = toSellerList(dcb, sellerName, startDate, endDate);

        return functionUtil.toPage(admSellerList, page, pageSize);
    }

    // 판매자 조회 엑셀 서비스 로직
    public void exportSellerListExcel(String dcb, String sellerName, String startDate, String endDate, HttpServletResponse response) throws IllegalAccessException, IOException {
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
        List<AdmSeller> admSellerList = toSellerList(dcb, sellerName, startDate, endDate);

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
    }

    private List<AdmSeller> toSellerList(String dcb, String sellerName, String startDate, String endDate) {
        if (sellerName.isEmpty()) {
            return searchSeller(startDate, endDate);
        } else {
            return searchSeller(sellerName, startDate, endDate);
        }
    }

    public void blockSeller(BlockSellerDto dto) {
        // TODO Okta 연동 후에 adminId 를 Session & HttpServletRequest 에서 가져오는 로직으로 변경할 것
        String adminId = "test12";

        List<String> sellerNameList = dto.getSellerNames();

        for (String s : sellerNameList) {
            AdmSeller admSeller = searchSeller(s);
            Map<String, String> requestMap = new HashMap<>();
            requestMap.put("adminId", adminId);
            requestMap.put("dcb", dto.getDcb()); // 아직 활용되지 않음
            requestMap.put("sellerName", admSeller.getSellerName());

            admSellerInfoMapper.blockSeller(requestMap);
        }
    }
}
