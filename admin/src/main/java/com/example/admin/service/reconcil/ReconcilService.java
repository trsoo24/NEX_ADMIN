package com.example.admin.service.reconcil;

import com.example.admin.domain.dto.reconcil.InsertReconcilDto;
import com.example.admin.domain.entity.reconcil.Reconcil;
import com.example.admin.repository.mapper.reconcil.ReconcilMapper;
import com.example.admin.common.service.FunctionUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReconcilService {
    private final ReconcilMapper reconcilMapper;
    private final FunctionUtil functionUtil;

    public void insertReconcil(InsertReconcilDto dto) {
        reconcilMapper.insertReconcil(dto);
    }
    public Page<Reconcil> getReconcilPage(String dcb, String startDate, String endDate, String fileType, int page, int pageSize) {
        return transReconcilListToPage(getReconcilList(dcb, startDate, endDate, fileType), page, pageSize);
    }

    public List<Reconcil> getReconcilList(String dcb, String startDate, String endDate, String fileType) {
        // get dcb, startDate, endDate, selectedFileType, page, pageSize
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("dcb", dcb);
        requestMap.put("startDate", startDate);
        requestMap.put("endDate", endDate);
        requestMap.put("fileType", fileType);
        return reconcilMapper.getReconcilList(requestMap);
    }

    public Page<Reconcil> transReconcilListToPage(List<Reconcil> reconcilList, int page, int pageSize) {
        return functionUtil.toPage(reconcilList, page, pageSize);
    }

    public void exportExcel(String dcb, String startDate, String endDate, String fileType, HttpServletResponse response) {
        List<Reconcil> reconcilList = getReconcilList(dcb, startDate, endDate, fileType);

        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet("대사 파일 조회");
        sheet.createRow(0);
        XSSFRow headerRow = sheet.createRow(1);


    }
}
