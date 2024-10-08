package com.example.admin.controller;

import com.example.admin.common.response.MapResult;
import com.example.admin.domain.entity.gdcb.ProvisioningInfo;
import com.example.admin.domain.entity.gdcb.SmsInfo;
import com.example.admin.service.voc.VocService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public void insertProvisioningInfo(@RequestBody @Valid ProvisioningInfo provisioningInfo) {
        vocService.insertProvisioningInfo(provisioningInfo);
    }
}
