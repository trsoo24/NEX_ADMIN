package com.example.admin.voc.controller;

import com.example.admin.common.response.MapResult;
import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.voc.dto.InsertVocDivision;
import com.example.admin.voc.dto.UpdateVocHistoryDto;
import com.example.admin.voc.dto.ProvisioningInfo;
import com.example.admin.voc.dto.SmsInfo;
import com.example.admin.voc.dto.VocDivision;
import com.example.admin.voc.service.VocService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/voc/history")
public class VocController {
    private final VocService vocService;

    @GetMapping("/conversion")
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

    @PostMapping("/voc")
    public StatusResult insertVocHistory(@RequestBody @Valid InsertVocDivision insertVocDivision) {
        vocService.insertVocDivision(insertVocDivision);

        return new StatusResult(true);
    }

    @PutMapping("/voc")
    public StatusResult updateVoccHistory(@RequestBody @Valid UpdateVocHistoryDto updateVocHistoryDto) {
        vocService.updateVocDivision(updateVocHistoryDto);

        return new StatusResult(true);
    }

    @GetMapping("/voc")
    public PageResult<VocDivision> getVocHistoryPage(@RequestParam("dcb") @Valid String dcb,
                                                     @RequestParam("writer") @Valid String writer,
                                                     @RequestParam("startDate") @Valid String startDate,
                                                     @RequestParam("endDate") @Valid String endDate,
                                                     @RequestParam("page") @Valid int page,
                                                     @RequestParam("pageSize") @Valid int pageSize) {
        Page<VocDivision> vocDivisionPage = vocService.getVocDivisionList(dcb, writer, startDate, endDate, page, pageSize);

        return new PageResult<>(true, vocDivisionPage);
    }

    @DeleteMapping("/voc")
    public StatusResult deleteVocHistory(@RequestParam("ids") @Valid List<Integer> ids) {
        vocService.deleteVocHistory(ids);

        return new StatusResult(true);
    }
}
