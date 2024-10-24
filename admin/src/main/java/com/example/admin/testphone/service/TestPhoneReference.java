package com.example.admin.testphone.service;

import com.example.admin.testphone.mapper.TestPhoneMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestPhoneReference {
    private final TestPhoneMapper testPhoneMapper;

    public boolean existsCtn(String ctn) {
        return testPhoneMapper.existsCtn(ctn);
    }
}
