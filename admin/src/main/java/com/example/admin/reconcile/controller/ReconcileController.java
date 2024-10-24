package com.example.admin.reconcile.controller;

import com.example.admin.common.response.MapResult;
import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.reconcile.dto.GDCBDetailCompare;
import com.example.admin.reconcile.dto.InsertReconcileDto;
import com.example.admin.reconcile.dto.Reconcile;
import com.example.admin.reconcile.service.GDCBInvoiceDetailService;
import com.example.admin.reconcile.service.GDCBReconcileService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reconciliation")
public class ReconcileController {
    private final GDCBReconcileService gdcbReconcileService;
    private final GDCBInvoiceDetailService gdcbInvoiceDetailService;

    @PostMapping("/gdcb")
    public StatusResult insertReconcile(@RequestBody @Valid InsertReconcileDto insertReconcileDto) {
        gdcbReconcileService.insertReconcile(insertReconcileDto);

        return new StatusResult(true);
    }

    @GetMapping("/gdcb")
    public PageResult<Reconcile> getGDCBReconcilePage(@RequestParam("dcb") @Valid String dcb,
                                                  @RequestParam("month") @Valid String month,
                                                  @RequestParam("fileType") @Valid String fileType,
                                                  @RequestParam("page") @Valid int page,
                                                  @RequestParam("pageSize") @Valid int pageSize) {
        Page<Reconcile> reconcilPage = gdcbReconcileService.getGDCBReconcilePage(dcb, month, fileType, page, pageSize);

        return new PageResult<>(true, reconcilPage);
    }

    @GetMapping("/excel/gdcb")
    public void getGDCBReconcileExcel(@RequestParam("dcb") @Valid String dcb,
                                  @RequestParam("month") @Valid String month,
                                  @RequestParam("fileType") @Valid String fileType,
                                  HttpServletResponse response) throws IOException {
        gdcbReconcileService.exportGDCBExcel(dcb, month, fileType, response);
    }

    @GetMapping("/gdcb/download")
    public void getGDCBReconcileFile(@RequestParam("dcb") @Valid String dcb,
                                     @RequestParam("month") @Valid String month,
                                     @RequestParam("fileType") @Valid String fileType,
                                     @RequestParam("fileName") @Valid String fileName, HttpServletResponse response) {
        gdcbReconcileService.getGDCBReconcileFile(dcb, month, fileType, fileName, response);
    }

    @GetMapping("/adjustment/gdcb")
    public MapResult<String, List<GDCBDetailCompare>> getGDCBInvoiceDetailList(@RequestParam("dcb") @Valid String dcb, @RequestParam("month") @Valid String month) {
        Map<String, List<GDCBDetailCompare>> invoiceDetailMap = gdcbInvoiceDetailService.getGDCBInvoiceDetailMap(dcb, month);

        return new MapResult<>(true, invoiceDetailMap);
    }

    @GetMapping("/adjustment/gdcb/excel")
    public void getGDCBInvoiceDetailExcel(@RequestParam("dcb") @Valid String dcb, @RequestParam("month") @Valid String month, HttpServletResponse response) throws IOException {
        gdcbInvoiceDetailService.exportInvoiceDetailExcel(dcb, month, response);
    }

    @PostMapping("/gdcb/add")
    public void insertGDCBInvoice(@RequestParam("year") String year) {
        gdcbInvoiceDetailService.insertInvoiceDetailData(year);
    }
}
