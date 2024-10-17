package com.example.admin.controller;

import com.example.admin.domain.dto.scheduler_status.SchedulerStatusResponse;
import com.example.admin.service.schduler_status.SchedulerStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/scheduler-status")
@RequiredArgsConstructor
public class SchedulerStatusController {

    private final SchedulerStatusService schedulerStatusService;


    @GetMapping()
    public SchedulerStatusResponse getSchedulerStatus() {
        return schedulerStatusService.getSchedulerStatus();
    }

}
