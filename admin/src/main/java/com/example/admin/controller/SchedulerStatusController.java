package com.example.admin.controller;

import com.example.admin.domain.dto.scheduler_status.ModifySchedulerStatusRequest;
import com.example.admin.domain.dto.scheduler_status.SchedulerStatusResponse;
import com.example.admin.service.schduler_status.SchedulerStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/scheduler-status")
@RequiredArgsConstructor
public class SchedulerStatusController {

    private final SchedulerStatusService schedulerStatusService;


    // 스케줄러의 기동 현황을 조회한다.
    @GetMapping()
    public List<SchedulerStatusResponse> getSchedulerStatus() {
        return schedulerStatusService.getSchedulerStatus();
    }


    // 스케줄러의 기동 서버를 변경한다.
    @PutMapping()
    public void updateSchedulerStatus(@RequestBody ModifySchedulerStatusRequest request) {
        schedulerStatusService.updateSchedulerStatus(request);
    }

}
