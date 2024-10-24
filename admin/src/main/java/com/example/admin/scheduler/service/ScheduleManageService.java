package com.example.admin.scheduler.service;

import com.example.admin.scheduler.dto.ModifyScheduleManageRequest;
import com.example.admin.scheduler.dto.GetScheduleManageResponse;
import com.example.admin.scheduler.mapper.ScheduleManageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleManageService {

    private final ScheduleManageMapper scheduleManageMapper;


    // 스케줄러 기동 현황을 조회한다.
    public List<GetScheduleManageResponse> getSchedulerStatus() {
        return scheduleManageMapper.getSchedulerStatus();
    }


    // 스케줄러의 기동 서버를 변경한다.
    public void updateSchedulerStatus(ModifyScheduleManageRequest request) {
        scheduleManageMapper.updateSchedulerStatus(request);
    }

}
