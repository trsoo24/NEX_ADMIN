package com.example.admin.domain.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteEventDto {
    private String dcb;
    private List<String> eventNames;
}
