package com.example.admin.service.schduler_status;

import com.example.admin.domain.dto.scheduler_status.ModifySchedulerStatusRequest;
import com.example.admin.domain.dto.scheduler_status.SchedulerStatusResponse;
import com.example.admin.repository.mapper.scheduler_status.SchedulerStatusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulerStatusService {

    private final SchedulerStatusMapper schedulerStatusMapper;


    // 스케줄러 기동 현황을 조회한다.
    public SchedulerStatusResponse getSchedulerStatus() {
        return schedulerStatusMapper.getSchedulerStatus();
    }


    // 스케줄러의 기동 서버를 변경한다.
    public void updateSchedulerStatus(ModifySchedulerStatusRequest request) {
        schedulerStatusMapper.updateSchedulerStatus(request);
    }

}
