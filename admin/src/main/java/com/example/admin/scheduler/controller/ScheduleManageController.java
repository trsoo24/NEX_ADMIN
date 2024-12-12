package com.example.admin.scheduler.controller;

import com.example.admin.common.response.ListResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.scheduler.dto.ModifyScheduleManageRequest;
import com.example.admin.scheduler.dto.GetScheduleManageResponse;
import com.example.admin.scheduler.service.ScheduleManageService;
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
@RequestMapping("/schedule-manage")
@RequiredArgsConstructor
public class ScheduleManageController {

    private final ScheduleManageService scheduleManageService;


    // 스케줄러의 기동 현황을 조회한다.
    @GetMapping()
    public ListResult<GetScheduleManageResponse> getSchedulerStatus() {
        return new ListResult<>(true, scheduleManageService.getSchedulerStatus());
    }


    // 스케줄러의 기동 서버를 변경한다.
    @PutMapping()
    public StatusResult updateSchedulerStatus(@RequestBody ModifyScheduleManageRequest request) {
        scheduleManageService.updateSchedulerStatus(request);

        return new StatusResult(true);
    }

}
