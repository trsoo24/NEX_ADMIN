package com.example.admin.controller;

import com.example.admin.common.response.PageResult;
import com.example.admin.domain.entity.message.MmsInfo;
import com.example.admin.service.voc.VocService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "8")
@RestController
@RequiredArgsConstructor
@RequestMapping("/voc")
public class VocController {
    private final VocService vocService;

    @GetMapping("/history/mms")
    public PageResult<MmsInfo> getMmsHistory(@RequestParam("dcb") @Valid String dcb,
                                             @RequestParam("ctn") @Valid String ctn,
                                             @RequestParam("page") @Valid int page,
                                             @RequestParam("pageSize") @Valid int pageSize) {
        Page<MmsInfo> mmsInfoPage = vocService.getSmsInfoPage(dcb, ctn, page, pageSize);

        return new PageResult<>(true, mmsInfoPage);
    }
}