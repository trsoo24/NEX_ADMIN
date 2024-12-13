package com.example.admin.testphone.service;

import com.example.admin.testphone.dto.DeleteTestPhoneDto;
import com.example.admin.testphone.dto.InsertTestPhoneDto;
import com.example.admin.testphone.dto.TestPhone;
import com.example.admin.common.exception.TestPhoneException;
import com.example.admin.testphone.mapper.TestPhoneMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.admin.common.exception.enums.MemberErrorCode.DUPLICATED_CTN;
import static com.example.admin.common.exception.enums.TestPhoneErrorCode.NOT_IN_DB;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestPhoneService {
    private final TestPhoneMapper testPhoneMapper;

    public void insertTestPhone(InsertTestPhoneDto dto) {
        String trxNo = MDC.get("trxNo");

        log.info("[{}] 요청 = 테스트폰 생성", trxNo);

        boolean insertResponse = testPhoneMapper.insertTestPhone(dto);

        if (insertResponse) {
            log.info("[{}] 응답 = 테스트폰 생성 완료 CTN = {}", trxNo, dto.getCtn());
        } else {
            log.info("[{}] 응답 = 테스트폰 생성 실패", trxNo);
        }
    }

    public List<TestPhone> getAllTestPhones() {
        String trxNo = MDC.get("trxNo");

        log.info("[{}] 요청 = 테스트폰 전체 조회", trxNo);
        List<TestPhone> testPhoneList = testPhoneMapper.getAllTestPhone();

        log.info("[{}] 응답 = 테스트폰 {} 건 조회 완료", trxNo, testPhoneList.size());

        return testPhoneList;
    }

    @Transactional
    public void deleteTestPhone(DeleteTestPhoneDto dto) {
        String trxNo = MDC.get("trxNo");

        List<String> ctnList = dto.getCtns();

        log.info("[{}] 요청 = 테스트폰 {} 건 삭제", trxNo, ctnList.size());
        for (String ctn : ctnList) {
            if (existsCtn(ctn)) {
                testPhoneMapper.deleteCtn(ctn);
            } else {
                log.info("[{}] 응답 = DB에 존재하지 않는 테스트폰 입니다. CTN = {} ", trxNo, ctn);
            }
        }

        log.info("[{}] 응답 = 테스트폰 {} 건 삭제 완료", trxNo, ctnList.size());
    }

    public boolean existsCtn(String ctn) {
        String trxNo = MDC.get("trxNo");

        boolean result = testPhoneMapper.existsCtn(ctn);

        if (result) {
            log.info("[{}] 응답 = {} 는 이미 DB 에 존재하는 차단 테스트폰입니다", trxNo, ctn);
        } else {
            log.info("[{}] DB 내 중복 데이터 없음 ", trxNo);
        }

        return result;
    }
}
