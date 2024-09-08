package com.example.admin.service.voc;

import com.example.admin.domain.entity.message.MmsInfo;
import com.example.admin.repository.mapper.voc.VocMapper;
import com.example.admin.service.FunctionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VocService {
    private final VocMapper vocMapper;
    private final FunctionUtil functionUtil;

    public Page<MmsInfo> getSmsInfoPage(String dcb, String ctn, int page, int pageSize) {
        return functionUtil.toPage(getSmsInfoList(dcb, ctn), page, pageSize);
    }

    private List<MmsInfo> getSmsInfoList(String dcb, String ctn) {
        Map<String, String> map = new HashMap<>();
        map.put("dcb", dcb);
        map.put("ctn", ctn);

        return vocMapper.getMmsHistoryByCtn(map);
    }
}
