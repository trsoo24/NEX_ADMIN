package com.example.admin.mms_history.service;

import com.example.admin.mms_history.dto.MmsHistoryDto;
import com.example.admin.mms_history.mapper.MmsSendMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MmsSendHistoryService {
    private final MmsSendMapper mmsSendMapper;

    public List<MmsHistoryDto> getMmsSendHistoryList(String ctn, String startDate, String endDate) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("startDate", startDate);
        requestMap.put("endDate", endDate);
        requestMap.put("ctn", ctn);

        List<MmsHistoryDto> mmsHistoryDtoList = mmsSendMapper.selectMmsHistoryList(requestMap);

        for (MmsHistoryDto mmsHistoryDto : mmsHistoryDtoList) {
            mmsHistoryDto.setCtnBlind();
        }

        return mmsHistoryDtoList;
    }
}
