package com.example.admin.refund.controller;

import com.example.admin.common.response.ListResult;
import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.refund.dto.RefundDto;
import com.example.admin.auth.dto.AuthInfo;
import com.example.admin.refund.dto.ManualRefund;
import com.example.admin.refund.dto.ManualRefundFileInfo;
import com.example.admin.refund.dto.RefundProcessDto;
import com.example.admin.refund.service.GdcbRefundService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/refund")
@RequiredArgsConstructor
public class RefundController {
    private final GdcbRefundService gdcbRefundService;

    @PostMapping("/auth")
    public void insertGdcbAuthInfo(@RequestBody AuthInfo authInfo) {
        gdcbRefundService.insertGdcbAuthInfo(authInfo);
    }

    @PostMapping("/manualRefund")
    public void insertManualRefund(@RequestBody ManualRefund manualRefund) {
        gdcbRefundService.insertManualRefund(manualRefund);
    }

    @PostMapping("/manualRefund/file")
    public void insertManualRefundFile(@RequestBody ManualRefundFileInfo manualRefundFileInfo) {
        gdcbRefundService.insertManualRefundFileInfo(manualRefundFileInfo);
    }

    @GetMapping("/gdcb")
    public PageResult<RefundDto> getGdcbRefundHistory(@RequestParam("correlationId") @Valid String correlationId,
                                                      @RequestParam("page") int page,
                                                      @RequestParam("pageSize") int pageSize) throws Exception {
        Page<RefundDto> refundDtoPage = gdcbRefundService.getRefundDtoList(correlationId, page, pageSize);

        return new PageResult<>(true, refundDtoPage);
    }

    @PostMapping("/process/gdcb") // GDCB 환불 프로세스 실행
    public StatusResult refundProcess(HttpServletRequest request, @RequestBody RefundProcessDto refundProcessDto) {
        boolean done = gdcbRefundService.refundProcess(request, refundProcessDto);

        return new StatusResult(done);
    }


    @PutMapping("/gdcb/{correlationId}")
    public StatusResult updateManualRefund(@PathVariable(name = "correlationId") @Valid String correlationId) {
        gdcbRefundService.updateRefundAuth("gdcb", correlationId);

        return new StatusResult(true);
    }
}