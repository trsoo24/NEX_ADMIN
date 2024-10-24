package com.example.admin.controller;

import com.example.admin.common.response.PageResult;
import com.example.admin.domain.dto.mms.MmsHistoryDto;
import com.example.admin.domain.entity.mms.MmsSendHistory;
import com.example.admin.service.mms.MmsSendHistoryService;
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
                                                       @RequestParam("page") @Valid int page,
                                                       @RequestParam("pageSize") @Valid int pageSize) {
        Page<MmsHistoryDto> mmsHistoryDtoPage = mmsSendHistoryService.getMmsSendHistoryPage(ctn, page, pageSize);

        return new PageResult<>(true, mmsHistoryDtoPage);
    }

    @GetMapping("/history/mms/excel")
    public void exportMmsHistoryExcel(@RequestParam("ctn") @Valid String ctn,
                                      HttpServletResponse response) throws IOException {
        mmsSendHistoryService.exportExcel(ctn, response);
    }
}
