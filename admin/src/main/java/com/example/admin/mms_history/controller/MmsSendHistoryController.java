package com.example.admin.mms_history.controller;

import com.example.admin.mms_history.dto.MmsHistoryDto;
import com.example.admin.mms_history.service.MmsSendHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/voc")
@RequiredArgsConstructor
public class MmsSendHistoryController {
    private final MmsSendHistoryService mmsSendHistoryService;


    @GetMapping("/history/mms")
    public List<MmsHistoryDto> getMmsHistoryPage(@RequestParam("ctn") @Valid String ctn,
                                                 @RequestParam("startDate") @Valid String startDate,
                                                 @RequestParam("endDate") @Valid String endDate) {
        return mmsSendHistoryService.getMmsSendHistoryList(ctn, startDate, endDate);
    }
}
