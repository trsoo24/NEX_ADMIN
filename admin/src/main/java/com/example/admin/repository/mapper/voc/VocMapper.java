package com.example.admin.repository.mapper.voc;

import com.example.admin.domain.entity.message.MmsInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface VocMapper {
    void insertMms(MmsInfo mmsInfo);
    List<MmsInfo> getMmsHistoryByCtn(Map<String, String> map);
}
