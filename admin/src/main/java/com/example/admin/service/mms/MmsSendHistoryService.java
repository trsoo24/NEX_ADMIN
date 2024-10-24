package com.example.admin.service.mms;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.domain.dto.mms.MmsHistoryDto;
import com.example.admin.domain.entity.mms.MmsSendHistory;
import com.example.admin.repository.mapper.mms.MmsSendMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MmsSendHistoryService {
    private final MmsSendMapper mmsSendMapper;
    private final FunctionUtil functionUtil;
    private final String[] header = {"문자 발송 일시", "CTN", "발송 내용", "발송 결과"};

    public Page<MmsHistoryDto> getMmsSendHistoryPage(String ctn, int page, int pageSize) {
        List<MmsHistoryDto> mmsHistoryDtoList = getMmsSendHistoryList(ctn);

        return functionUtil.toPage(mmsHistoryDtoList, page, pageSize);
    }

    private List<MmsHistoryDto> getMmsSendHistoryList(String ctn) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("ctn", ctn);

        List<MmsHistoryDto> mmsHistoryDtoList = mmsSendMapper.selectMmsHistoryList(requestMap);

        for (MmsHistoryDto mmsHistoryDto : mmsHistoryDtoList) {
            mmsHistoryDto.setCtnBlind();
        }

        return mmsHistoryDtoList;
    }

    public void insertMmsSendHistory(MmsSendHistory mmsSendHistory) {
        mmsSendMapper.insertMmsHistory(mmsSendHistory);
    }


    public void exportExcel(String ctn, HttpServletResponse response) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("문자 발송 이력 조회");
        XSSFCellStyle middle = workbook.createCellStyle();
        middle.setAlignment(HorizontalAlignment.CENTER);

        List<MmsHistoryDto> mmsHistoryDtoList = getMmsSendHistoryList(ctn);

        int rowIdx = 0;
        int colIdx = 0;

        for (int i = 0; i < header.length; i++) {
            XSSFRow row = sheet.getRow(rowIdx);

            if (row == null) {
                row = sheet.createRow(rowIdx);
            }

            row.createCell(colIdx++).setCellValue(header[i]);
        }

        rowIdx++;
        colIdx = 0;

        for (int i = 0; i < mmsHistoryDtoList.size(); i++) {
            MmsHistoryDto mmsHistoryDto = mmsHistoryDtoList.get(i);

            XSSFRow row = sheet.getRow(rowIdx);

            if (row == null) {
                row = sheet.createRow(rowIdx);
            }

            row.createCell(colIdx++).setCellValue(mmsHistoryDto.getCreateDt());
            row.createCell(colIdx++).setCellValue(mmsHistoryDto.getCtn());
            row.createCell(colIdx++).setCellValue(mmsHistoryDto.getContent());
            row.createCell(colIdx).setCellValue(mmsHistoryDto.getSendResult());

            rowIdx++;
            colIdx = 0;
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=MmsSendHistory.xlsx");
        response.setStatus(200);
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();
    }
}
