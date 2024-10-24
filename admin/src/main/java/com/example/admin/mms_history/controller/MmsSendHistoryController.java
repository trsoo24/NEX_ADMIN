package com.example.admin.mms_history.controller;

import com.example.admin.common.response.PageResult;
import com.example.admin.mms_history.dto.MmsHistoryDto;
import com.example.admin.mms_history.dto.MmsSendHistory;
import com.example.admin.mms_history.service.MmsSendHistoryService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/voc")
@RequiredArgsConstructor
public class MmsSendHistoryController {
    private final MmsSendHistoryService mmsSendHistoryService;

    @PostMapping("/mms")
    public void insertSendHistory(@RequestBody MmsSendHistory mmsSendHistory) {
        mmsSendHistoryService.insertMmsSendHistory(mmsSendHistory);
    }

    @GetMapping("/history/mms")
    public PageResult<MmsHistoryDto> getMmsHistoryPage(@RequestParam("ctn") @Valid String ctn,
                                                       @RequestParam("startDate") @Valid String startDate,
                                                       @RequestParam("endDate") @Valid String endDate,
                                                       @RequestParam("page") @Valid int page,
                                                       @RequestParam("pageSize") @Valid int pageSize) {
        Page<MmsHistoryDto> mmsHistoryDtoPage = mmsSendHistoryService.getMmsSendHistoryPage(ctn, startDate, endDate, page, pageSize);

        return new PageResult<>(true, mmsHistoryDtoPage);
    }

    @GetMapping("/history/mms/excel")
    public void exportMmsHistoryExcel(@RequestParam("ctn") @Valid String ctn,
                                      @RequestParam("startDate") @Valid String startDate,
                                      @RequestParam("endDate") @Valid String endDate,
                                      HttpServletResponse response) throws IOException {
        mmsSendHistoryService.exportExcel(ctn, startDate, endDate, response);
    }
}
