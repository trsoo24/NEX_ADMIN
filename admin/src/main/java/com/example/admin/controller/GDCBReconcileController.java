package com.example.admin.controller;

import com.example.admin.common.response.MapResult;
import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.domain.dto.reconcile.gdcb.GDCBMonthlyInvoiceSum;
import com.example.admin.domain.dto.reconcile.gdcb.InsertReconcileDto;
import com.example.admin.domain.entity.reconcile.gdcb.MonthlyInvoiceSum;
import com.example.admin.domain.entity.reconcile.gdcb.Reconcile;
import com.example.admin.service.reconcile.gdcb.GDCBReconcileService;
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
public class GDCBReconcileController {
    private final GDCBReconcileService GDCBReconcileService;

    @PostMapping("/gdcb")
    public StatusResult insertReconcile(@RequestBody @Valid InsertReconcileDto insertReconcileDto) {
        GDCBReconcileService.insertReconcile(insertReconcileDto);

        return new StatusResult(true);
    }

    @GetMapping("/gdcb")
    public PageResult<Reconcile> getGDCBReconcilePage(@RequestParam("dcb") @Valid String dcb,
                                                  @RequestParam("month") @Valid String month,
                                                  @RequestParam("fileType") @Valid String fileType,
                                                  @RequestParam("page") @Valid int page,
                                                  @RequestParam("pageSize") @Valid int pageSize) {
        Page<Reconcile> reconcilPage = GDCBReconcileService.getGDCBReconcilePage(dcb, month, fileType, page, pageSize);

        return new PageResult<>(true, reconcilPage);
    }

    @GetMapping("/excel/gdcb")
    public void getGDCBReconcileExcel(@RequestParam("dcb") @Valid String dcb,
                                  @RequestParam("month") @Valid String month,
                                  @RequestParam("fileType") @Valid String fileType,
                                  HttpServletResponse response) throws IOException {
        GDCBReconcileService.exportGDCBExcel(dcb, month, fileType, response);
    }

    @GetMapping("/gdcb/download")
    public void getGDCBReconcileFile(@RequestParam("dcb") @Valid String dcb,
                                     @RequestParam("month") @Valid String month,
                                     @RequestParam("fileType") @Valid String fileType,
                                     @RequestParam("fileName") @Valid String fileName, HttpServletResponse response) {
        GDCBReconcileService.getGDCBReconcileFile(dcb, month, fileType, fileName, response);
    }

    @GetMapping("/gdcb/invoice")
    public MapResult<String, List<MonthlyInvoiceSum>> getGDCBInvoiceDetailList(@RequestParam("dcb") @Valid String dcb, @RequestParam("month") @Valid String month) {
        return null;
    }
}
