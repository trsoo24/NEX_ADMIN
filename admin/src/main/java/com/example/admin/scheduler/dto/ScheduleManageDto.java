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
    private final String dcb = "GDCB";
    private String scheduleCd;
    private String scheduleServer;
    private String redId;
    private String regDt;
    private String creDt;
}
