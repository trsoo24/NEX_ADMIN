package com.example.admin.controller;

import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.domain.dto.reconcil.InsertReconcilDto;
import com.example.admin.domain.entity.reconcil.Reconcil;
import com.example.admin.service.reconcil.ReconcilService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reconciliation")
public class ReconcilController {
    private final ReconcilService reconcilService;

    @PostMapping
    public StatusResult insertReconcil(@RequestBody @Valid InsertReconcilDto insertReconcilDto) {
        reconcilService.insertReconcil(insertReconcilDto);

        return new StatusResult(true);
    }

    @GetMapping
    public PageResult<Reconcil> getReconcilPage(@RequestParam @Valid String dcb,
                                                @RequestParam @Valid String startDate,
                                                @RequestParam @Valid String endDate,
                                                @RequestParam @Valid String fileType,
                                                @RequestParam @Valid int page,
                                                @RequestParam @Valid int pageSize) {
        Page<Reconcil> reconcilPage = reconcilService.getReconcilPage(dcb, startDate, endDate, fileType, page, pageSize);

        return new PageResult<>(true, reconcilPage);
    }

    @GetMapping("/excel")
    public void getReconcilExcel(@RequestParam @Valid String dcb,
                                          @RequestParam @Valid String startDate,
                                          @RequestParam @Valid String endDate,
                                          @RequestParam @Valid String fileType,
                                          HttpServletResponse response) {
        reconcilService.exportExcel(dcb, startDate, endDate, fileType, response);
    }
}
