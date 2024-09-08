package com.example.admin.domain.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertEventDto {
    private String dcb;
    private String eventName;
}
