package com.example.admin.mms_history.mapper;

import com.example.admin.mms_history.dto.MmsHistoryDto;
import com.example.admin.mms_history.dto.MmsSendHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MmsSendMapper {
    void insertMmsHistory(MmsSendHistory mmsSendHistory);

    List<MmsHistoryDto> selectMmsHistoryList(Map<String, Object> map);
}
