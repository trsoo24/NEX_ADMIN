package com.example.admin.repository.mapper.mms;

import com.example.admin.domain.dto.mms.MmsHistoryDto;
import com.example.admin.domain.entity.mms.MmsSendHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MmsSendMapper {
    void insertMmsHistory(MmsSendHistory mmsSendHistory);

    List<MmsHistoryDto> selectMmsHistoryList(Map<String, Object> map);
}
