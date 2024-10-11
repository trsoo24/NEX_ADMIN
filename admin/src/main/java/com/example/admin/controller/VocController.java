package com.example.admin.controller;

import com.example.admin.common.response.MapResult;
import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.domain.dto.voc.InsertVocClassification;
import com.example.admin.domain.dto.voc.UpdateVocHistoryDto;
import com.example.admin.domain.entity.gdcb.ProvisioningInfo;
import com.example.admin.domain.entity.gdcb.SmsInfo;
import com.example.admin.domain.entity.voc.VocClassification;
import com.example.admin.service.voc.VocService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/voc")
public class VocController {
    private final VocService vocService;

    @GetMapping("/history/conversion")
    public MapResult<String, Object> getConversionHistory(@RequestParam("dcb") @Valid String dcb,
                                                          @RequestParam("ctn") @Valid String ctn) {
        Map<String, Object> responseMap = vocService.getGdcbConversionHistory(dcb, ctn);

        return new MapResult<>(true, responseMap);
    }


    @PostMapping("/sms")
    public void insertSmsInfo(@RequestBody @Valid SmsInfo smsInfo) {
        vocService.insertSmsInfo(smsInfo);
    }

    @PostMapping("/provisioning")
    public StatusResult insertProvisioningInfo(@RequestBody @Valid ProvisioningInfo provisioningInfo) {
        vocService.insertProvisioningInfo(provisioningInfo);

        return new StatusResult(true);
    }

    @PostMapping("/classification")
    public StatusResult insertVocClassification(@RequestBody @Valid InsertVocClassification insertVocClassification) {
        vocService.insertVocClassification(insertVocClassification);

        return new StatusResult(true);
    }

    @PutMapping("/classification")
    public StatusResult updateVocClassification(@RequestBody @Valid UpdateVocHistoryDto updateVocHistoryDto) {
        vocService.updateVocClassification(updateVocHistoryDto);

        return new StatusResult(true);
    }

    @GetMapping("/classification")
    public PageResult<VocClassification> getVocClassificationPage(@RequestParam("dcb") @Valid String dcb,
                                                                  @RequestParam("ctn") @Valid String ctn,
                                                                  @RequestParam("page") @Valid int page,
                                                                  @RequestParam("pageSize") @Valid int pageSize) {
        Page<VocClassification> vocClassificationPage = vocService.getVocClassificationList(dcb, ctn, page, pageSize);

        return new PageResult<>(true, vocClassificationPage);
    }
}
