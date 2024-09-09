package com.example.admin.service.event;

import com.example.admin.domain.dto.event.DeleteEventDto;
import com.example.admin.domain.dto.event.InsertEventDto;
import com.example.admin.domain.entity.event.Event;
import com.example.admin.repository.mapper.event.EventMapper;
import com.example.admin.service.FunctionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventMapper eventMapper;
    private final FunctionUtil functionUtil;

    public void insertEvent(InsertEventDto dto) {
        Map<String, String> map = new HashMap<>();
        map.put("eventName", dto.getEventName());
        // cookie 에서 token 으로 값 넣기
        map.put("regId", "");
        map.put("dcb", dto.getDcb());

        eventMapper.insertEvent(map);
    }

    public Page<Event> getEventPage(String dcb, String eventName, int page, int pageSize) {
        return functionUtil.toPage(getEventList(dcb, eventName), page, pageSize);
    }

    public void deleteEvent(DeleteEventDto dto) {
        for (String eventName : dto.getEvents()) {
            Map<String, String> map = new HashMap<>();
            map.put("eventName", eventName);
            map.put("dcb", dto.getDcb());

            if (existsEvent(map)) {
                eventMapper.deleteEvent(map);
            }
        }
    }

    private boolean existsEvent(Map<String, String> map) {
        return eventMapper.existsEvent(map);
    }

    private List<Event> getEventList(String dcb, String eventName) {
        Map<String, String> map = new HashMap<>();
        map.put("eventName", eventName);
        map.put("dcb", dcb);

        return eventMapper.getEventList(map);
    }
}
