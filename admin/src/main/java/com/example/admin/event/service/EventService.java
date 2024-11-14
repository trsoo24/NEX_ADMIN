package com.example.admin.event.service;

import com.example.admin.event.dto.DeleteEventDto;
import com.example.admin.event.dto.InsertEventDto;
import com.example.admin.event.dto.UpdateEventDto;
import com.example.admin.event.dto.Event;
import com.example.admin.event.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventMapper eventMapper;

    // Event 생성
    public void insertEvent(InsertEventDto dto) {
        eventMapper.insertEvent(dto);
    }

    // Event 삭제
    public void deleteEvent(DeleteEventDto dto) {
        eventMapper.deleteEvent(dto.getEventNames());
    }

    // Event 조회
    public List<Event> getEventList(String eventName) {
        Map<String, String> map = new HashMap<>();
        map.put("eventName", eventName);

        return eventMapper.getEventList(map);
    }

    // Event 수정
    public void updateEvent(UpdateEventDto updateEventDto) {
        eventMapper.updateEvent(updateEventDto);
    }
}
