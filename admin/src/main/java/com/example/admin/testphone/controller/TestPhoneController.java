package com.example.admin.testphone.controller;


import com.example.admin.common.response.StatusResult;
import com.example.admin.testphone.dto.DeleteTestPhoneDto;
import com.example.admin.testphone.dto.InsertTestPhoneDto;
import com.example.admin.testphone.dto.TestPhone;
import com.example.admin.testphone.service.TestPhoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/testphones")
@Slf4j
public class TestPhoneController {
    private final TestPhoneService testPhoneService;

    @PostMapping()
    public StatusResult insertTestPhone(@RequestBody @Valid InsertTestPhoneDto dto) {
        testPhoneService.insertTestPhone(dto);

        return new StatusResult(true);
    }

    @GetMapping()
    public List<TestPhone> getAllTestPhones() {
        return testPhoneService.getAllTestPhones();
    }

    @DeleteMapping()
    public StatusResult dropTestPhone(@RequestBody @Valid DeleteTestPhoneDto dto) {
        testPhoneService.deleteTestPhone(dto);

        return new StatusResult(true);
    }

    @GetMapping("/check")
    public StatusResult checkExistTestPhone(@RequestParam("ctn") String ctn) {
        boolean result = testPhoneService.existsCtn(ctn);

        return new StatusResult(result);
    }
}
