package com.example.admin.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetScheduleManageResponse {
    private final String dcb = "GDCB";
    private String scheduleCd;
    private String scheduleServer;
    private String redId;
    private String regDt;
    private String creDt;
}
