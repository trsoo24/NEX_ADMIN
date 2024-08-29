package com.example.admin.controller;


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
    public void insertTestPhone(@RequestBody @Valid InsertTestPhoneDto dto) {
        testPhoneService.insertTestPhone(dto);
    }

    @GetMapping()
    public Page<TestPhone> getAllTestPhones(@RequestParam("dcb") @Valid String dcb, @RequestParam("page") @Valid Integer page, @RequestParam("pageSize") @Valid Integer pageSize, HttpServletRequest request) {
        log.info(request.getRequestURL() + request.getQueryString());
        return testPhoneService.getAllTestPhones(page, pageSize, dcb);
    }

    @DeleteMapping()
    public void dropTestPhone(@RequestParam("dcb") @Valid String dcb, @RequestParam("ctns") List<String> ctns) {
        testPhoneService.deleteTestPhone(ctns, dcb);
    }
}
