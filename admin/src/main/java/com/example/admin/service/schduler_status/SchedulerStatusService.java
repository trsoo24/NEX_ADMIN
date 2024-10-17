package com.example.admin.service.schduler_status;

import com.example.admin.domain.dto.scheduler_status.SchedulerStatusResponse;
import com.example.admin.repository.mapper.scheduler_status.SchedulerStatusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulerStatusService {

    private final SchedulerStatusMapper schedulerStatusMapper;


    public SchedulerStatusResponse getSchedulerStatus() {
        return schedulerStatusMapper.getSchedulerStatus();
    }

}
