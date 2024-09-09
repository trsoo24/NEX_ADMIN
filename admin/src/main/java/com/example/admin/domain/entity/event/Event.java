package com.example.admin.domain.entity.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private String eventName;
//    private String fileName; // 원본 파일명
//    private String url; // 이벤트 제공 URL
    private String regDt;
    private String redId;
    private String updDt;
    private String updId;
}
