package com.example.admin.event.service;

import com.example.admin.event.dto.DeleteEventDto;
import com.example.admin.event.dto.InsertEventDto;
import com.example.admin.event.dto.UpdateEventDto;
import com.example.admin.event.dto.Event;
import com.example.admin.event.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventMapper eventMapper;

    // Event 생성
    public void insertEvent(InsertEventDto dto) {
        String trxNo = MDC.get("trxNo");
        log.info("[{}] 요청 = 이벤트 생성 ", trxNo);

        eventMapper.insertEvent(dto);
    }

    // Event 삭제
    public void deleteEvent(DeleteEventDto dto) {
        String trxNo = MDC.get("trxNo");
        log.info("[{}] 요청 = 이벤트 삭제 ", trxNo);

        eventMapper.deleteEvent(dto.getEventNames());
    }

    // Event 조회
    public List<Event> getEventList(String eventName) {
        String trxNo = MDC.get("trxNo");
        log.info("[{}] 요청 = 이벤트 조회 ", trxNo);

        Map<String, String> map = new HashMap<>();
        map.put("eventName", eventName);

        return eventMapper.getEventList(map);
    }

    // Event 수정
    public void updateEvent(UpdateEventDto updateEventDto) {
        String trxNo = MDC.get("trxNo");
        log.info("[{}] 요청 = 이벤트 수정 ", trxNo);

        eventMapper.updateEvent(updateEventDto);
    }
}
