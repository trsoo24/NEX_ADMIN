package com.example.admin.voc.controller;

import com.example.admin.common.response.MapResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.voc.dto.ProvisioningInfo;
import com.example.admin.voc.service.VocService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/provisioning")
    public StatusResult insertProvisioningInfo(@RequestBody @Valid ProvisioningInfo provisioningInfo) {
        vocService.insertProvisioningInfo(provisioningInfo);

        return new StatusResult(true);
    }
}
