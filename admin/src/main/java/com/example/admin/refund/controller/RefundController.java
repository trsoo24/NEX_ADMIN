package com.example.admin.refund.controller;

import com.example.admin.common.response.ListResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.refund.dto.RefundDto;
import com.example.admin.auth.dto.AuthInfo;
import com.example.admin.refund.dto.ManualRefund;
import com.example.admin.refund.dto.ManualRefundFileInfo;
import com.example.admin.refund.service.GdcbRefundService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/purchase-history/gdcb")
    public ListResult<RefundDto> getGdcbRefundHistory(@RequestParam("dcb") @Valid String dcb,
                                                      @RequestParam("correlationId") @Valid String correlationId) throws Exception {
        List<RefundDto> refundDtoList = gdcbRefundService.getRefundDtoList(dcb, correlationId);

        return new ListResult<>(true, refundDtoList);
    }

    @PostMapping("/process/gdcb") // GDCB 환불 프로세스 실행
    public StatusResult refundProcess(HttpServletRequest request, @RequestBody RefundDto refundDto) {
        boolean done = gdcbRefundService.refundProcess(request, refundDto);

        return new StatusResult(done);
    }


    @PutMapping("/purchase-history/gdcb/{correlationId}")
    public StatusResult updateManualRefund(@PathVariable(name = "correlationId") @Valid String correlationId) {
        gdcbRefundService.updateRefundAuth("gdcb", correlationId);

        return new StatusResult(true);
    }
}
