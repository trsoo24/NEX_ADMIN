package com.example.admin.service.mms;

import com.example.admin.common.service.FunctionUtil;
import com.example.admin.domain.dto.mms.MmsHistoryDto;
import com.example.admin.domain.entity.mms.MmsSendHistory;
import com.example.admin.repository.mapper.mms.MmsSendMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MmsSendHistoryService {
    private final MmsSendMapper mmsSendMapper;
    private final FunctionUtil functionUtil;

    public Page<MmsHistoryDto> getMmsSendHistoryPage(String dcb, String ctn, int page, int pageSize) {
        List<MmsHistoryDto> mmsHistoryDtoList = getMmsSendHistoryList(dcb, ctn);

        return functionUtil.toPage(mmsHistoryDtoList, page, pageSize);
    }

    private List<MmsHistoryDto> getMmsSendHistoryList(String dcb, String ctn) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("dcb", dcb);
        requestMap.put("ctn", ctn);

        List<MmsHistoryDto> mmsHistoryDtoList = mmsSendMapper.selectMmsHistoryList(requestMap);

        for (MmsHistoryDto mmsHistoryDto : mmsHistoryDtoList) {
            mmsHistoryDto.setCtnBlind();
        }

        return mmsHistoryDtoList;
    }

    public void insertMmsSendHistory(MmsSendHistory mmsSendHistory) {
        mmsSendMapper.insertMmsHistory(mmsSendHistory);
    }
}
