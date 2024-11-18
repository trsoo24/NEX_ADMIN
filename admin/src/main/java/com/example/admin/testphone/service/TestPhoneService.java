package com.example.admin.testphone.service;

import com.example.admin.testphone.dto.DeleteTestPhoneDto;
import com.example.admin.testphone.dto.InsertTestPhoneDto;
import com.example.admin.testphone.dto.TestPhone;
import com.example.admin.common.exception.TestPhoneException;
import com.example.admin.testphone.mapper.TestPhoneMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.admin.common.exception.enums.MemberErrorCode.DUPLICATED_CTN;
import static com.example.admin.common.exception.enums.TestPhoneErrorCode.NOT_IN_DB;

@Service
@RequiredArgsConstructor
public class TestPhoneService {
    private final TestPhoneMapper testPhoneMapper;
    private final TestPhoneReference testPhoneReference;

    public void insertTestPhone(InsertTestPhoneDto dto) {
        if (testPhoneReference.existsCtn(dto.getCtn())) {
            throw new TestPhoneException(DUPLICATED_CTN);
        }

        testPhoneMapper.insertTestPhone(dto);
    }

    public List<TestPhone> getAllTestPhones() {
        return testPhoneMapper.getAllTestPhone();
    }

    @Transactional
    public void deleteTestPhone(DeleteTestPhoneDto dto) {
        List<String> ctnList = dto.getCtns();

        for (String ctn : ctnList) {
            if (testPhoneReference.existsCtn(ctn)) {
                testPhoneMapper.deleteCtn(ctn);
            } else {
                throw new TestPhoneException(NOT_IN_DB);
            }
        }
    }
}
