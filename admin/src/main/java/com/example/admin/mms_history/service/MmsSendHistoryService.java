package com.example.admin.mms_history.service;

import com.example.admin.mms_history.dto.MmsHistoryDto;
import com.example.admin.mms_history.mapper.MmsSendMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MmsSendHistoryService {
    private final MmsSendMapper mmsSendMapper;

    public List<MmsHistoryDto> getMmsSendHistoryList(String ctn, String startDate, String endDate) {
        String trxNo = MDC.get("trxNo");

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("startDate", startDate);
        requestMap.put("endDate", endDate);
        requestMap.put("ctn", ctn);

        log.info("[{}] 요청 = {} 부터 {} 까지 CTN = {} 문자 발송 이력 조회 ", trxNo, startDate, endDate, ctn);

        List<MmsHistoryDto> mmsHistoryDtoList = mmsSendMapper.selectMmsHistoryList(requestMap);

        for (MmsHistoryDto mmsHistoryDto : mmsHistoryDtoList) {
            mmsHistoryDto.setCtnBlind();
        }

        log.info("[{}] 응답 = 문자 발송 이력 {} 건 조회 완료", trxNo, mmsHistoryDtoList.size());

        return mmsHistoryDtoList;
    }
}
