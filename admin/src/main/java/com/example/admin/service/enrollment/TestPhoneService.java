package com.example.admin.service.enrollment;

import com.example.admin.domain.dto.enrollment.DeleteTestPhoneDto;
import com.example.admin.domain.dto.enrollment.InsertTestPhoneDto;
import com.example.admin.domain.entity.enrollment.TestPhone;
import com.example.admin.exception.TestPhoneException;
import com.example.admin.repository.mapper.enrollment.TestPhoneMapper;
import com.example.admin.service.reference.TestPhoneReference;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.admin.exception.enums.MemberErrorCode.DUPLICATED_CTN;
import static com.example.admin.exception.enums.TestPhoneErrorCode.NOT_IN_DB;

@Service
@RequiredArgsConstructor
public class TestPhoneService {
    private final TestPhoneMapper testPhoneMapper;
    private final TestPhoneReference testPhoneReference;

    public void insertTestPhone(InsertTestPhoneDto dto) {
        if (testPhoneReference.existsCtn(dto.getCtn())) {
            throw new TestPhoneException(DUPLICATED_CTN);
        }

        testPhoneMapper.insertTestPhone(TestPhone.toTestPhone(dto));
    }

    public Page<TestPhone> getAllTestPhones(int page, int pageSize, String dcb) {
        Map<String, Integer> map = new HashMap<>();
        map.put("offset", (page - 1) * pageSize);
        map.put("pageSize", pageSize);

        List<TestPhone> testPhoneList = testPhoneMapper.getAllTestPhone(map);
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        return new PageImpl<>(testPhoneList, pageable, testPhoneMapper.countCtn());
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
