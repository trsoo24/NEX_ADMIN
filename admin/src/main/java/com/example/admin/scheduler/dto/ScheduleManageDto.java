package com.example.admin.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleManageDto {
    private String scheduleCd;
    private String scheduleServer;
    private String regId;
    private String regDt;
    private String creDt;
}
