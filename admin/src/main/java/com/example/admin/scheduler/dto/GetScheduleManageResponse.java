package com.example.admin.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetScheduleManageResponse {
    private final String dcb = "GDCB";
    private String scheduler;
    private String server1;
    private String server2;
    private String lastModifier;
    private String lastModifiedDate;
}
