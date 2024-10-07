package com.example.admin.controller;

import com.example.admin.common.response.PageResult;
import com.example.admin.domain.dto.mms.MmsHistoryDto;
import com.example.admin.domain.entity.mms.MmsSendHistory;
import com.example.admin.service.mms.MmsSendHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voc")
@RequiredArgsConstructor
public class MmsSendHistoryController {
    private final MmsSendHistoryService mmsSendHistoryService;

    @PostMapping
    public void insertSendHistory(@RequestBody MmsSendHistory mmsSendHistory) {
        mmsSendHistoryService.insertMmsSendHistory(mmsSendHistory);
    }

    @GetMapping("/history/mms")
    public PageResult<MmsHistoryDto> getMmsHistoryPage(@RequestParam("dcb") @Valid String dcb,
                                                       @RequestParam("startDate") @Valid String startDate,
                                                       @RequestParam("endDate") @Valid String endDate,
                                                       @RequestParam("ctn") @Valid String ctn,
                                                       @RequestParam("page") @Valid int page,
                                                       @RequestParam("pageSize") @Valid int pageSize) {
        Page<MmsHistoryDto> mmsHistoryDtoPage = mmsSendHistoryService.getMmsSendHistoryPage(dcb, startDate, endDate, ctn, page, pageSize);

        return new PageResult<>(true, mmsHistoryDtoPage);
    }


}
