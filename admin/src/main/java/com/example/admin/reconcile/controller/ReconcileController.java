package com.example.admin.reconcile.controller;

import com.example.admin.common.response.MapResult;
import com.example.admin.reconcile.dto.GDCBDetailCompare;
import com.example.admin.reconcile.dto.Reconcile;
import com.example.admin.reconcile.service.GDCBInvoiceDetailService;
import com.example.admin.reconcile.service.GDCBReconcileService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public List<Reconcile> getGDCBReconcileList(@RequestParam("month") @Valid String month,
                                                  @RequestParam("fileType") @Valid String fileType) {
        return gdcbReconcileService.getGDCBReconcileList(month, fileType, false);
    }

    @GetMapping("/excel")
    public void getGDCBReconcileExcel(@RequestParam("month") @Valid String month,
                                  @RequestParam("fileType") @Valid String fileType,
                                  HttpServletResponse response) throws IOException {
        gdcbReconcileService.exportGDCBExcel(month, fileType, response);
    }

    @GetMapping("/download")
    public void getGDCBReconcileFile(@RequestParam("month") @Valid String month,
                                     @RequestParam("fileType") @Valid String fileType,
                                     @RequestParam("fileName") @Valid String fileName, HttpServletResponse response) {
        gdcbReconcileService.getGDCBReconcileFile(month, fileType, fileName, response);
    }

    @GetMapping("/adjustment")
    public MapResult<String, List<GDCBDetailCompare>> getGDCBInvoiceDetailList(@RequestParam("month") @Valid String month) {
        Map<String, List<GDCBDetailCompare>> invoiceDetailMap = gdcbInvoiceDetailService.getGDCBInvoiceDetailMap(month);

        return new MapResult<>(true, invoiceDetailMap);
    }
}
