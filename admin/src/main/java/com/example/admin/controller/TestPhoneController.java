package com.example.admin.controller;


import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.domain.dto.enrollment.DeleteTestPhoneDto;
import com.example.admin.domain.dto.enrollment.InsertTestPhoneDto;
import com.example.admin.domain.entity.enrollment.TestPhone;
import com.example.admin.service.enrollment.TestPhoneService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/testphone")
@Slf4j
public class TestPhoneController {
    private final TestPhoneService testPhoneService;

    @PostMapping()
    public StatusResult insertTestPhone(@RequestBody @Valid InsertTestPhoneDto dto) {
        testPhoneService.insertTestPhone(dto);

        return new StatusResult(true);
    }

    @GetMapping()
    public PageResult<TestPhone> getAllTestPhones(@RequestParam("dcb") @Valid String dcb, @RequestParam("page") @Valid Integer page, @RequestParam("pageSize") @Valid Integer pageSize) {
        Page<TestPhone> testPhonePage = testPhoneService.getAllTestPhones(page, pageSize, dcb);

        return new PageResult<>(true, testPhonePage);
    }

    @DeleteMapping()
    public StatusResult dropTestPhone(@RequestBody @Valid DeleteTestPhoneDto dto) {
        testPhoneService.deleteTestPhone(dto);

        return new StatusResult(true);
    }
}
