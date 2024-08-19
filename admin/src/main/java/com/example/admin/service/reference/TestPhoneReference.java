package com.example.admin.service.reference;

import com.example.admin.repository.mapper.enrollment.TestPhoneMapper;
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
