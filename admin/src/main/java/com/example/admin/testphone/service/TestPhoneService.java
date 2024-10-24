package com.example.admin.testphone.service;

import com.example.admin.testphone.dto.DeleteTestPhoneDto;
import com.example.admin.testphone.dto.InsertTestPhoneDto;
import com.example.admin.testphone.dto.TestPhone;
import com.example.admin.common.exception.TestPhoneException;
import com.example.admin.testphone.mapper.TestPhoneMapper;
import com.example.admin.common.service.FunctionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.admin.common.exception.enums.MemberErrorCode.DUPLICATED_CTN;
import static com.example.admin.common.exception.enums.TestPhoneErrorCode.NOT_IN_DB;

@Service
@RequiredArgsConstructor
public class TestPhoneService {
    private final TestPhoneMapper testPhoneMapper;
    private final TestPhoneReference testPhoneReference;
    private final FunctionUtil functionUtil;

    public void insertTestPhone(InsertTestPhoneDto dto) {
        if (testPhoneReference.existsCtn(dto.getCtn())) {
            throw new TestPhoneException(DUPLICATED_CTN);
        }

        testPhoneMapper.insertTestPhone(TestPhone.toTestPhone(dto));
    }

    public Page<TestPhone> getAllTestPhones(int page, int pageSize, String ctn, String dcb) {
        Map<String, Object> map = new HashMap<>();
        map.put("ctn", ctn);

        List<TestPhone> testPhoneList = testPhoneMapper.getAllTestPhone(map);

        return functionUtil.toPage(testPhoneList, page, pageSize);
    }

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
