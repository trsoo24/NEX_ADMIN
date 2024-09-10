package com.example.admin.service.event;

import com.example.admin.config.filter.CookieUtil;
import com.example.admin.config.filter.JwtTokenProvider;
import com.example.admin.domain.dto.event.DeleteEventDto;
import com.example.admin.domain.dto.event.InsertEventDto;
import com.example.admin.domain.dto.event.UpdateEventDto;
import com.example.admin.domain.entity.event.Event;
import com.example.admin.repository.mapper.event.EventMapper;
import com.example.admin.common.service.FunctionUtil;
import jakarta.servlet.http.HttpServletRequest;
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
    private final CookieUtil cookieUtil;
    private final JwtTokenProvider jwtTokenProvider;

    public void insertEvent(HttpServletRequest request, InsertEventDto dto) {
//        String token = cookieUtil.getAccessToken(request).getValue();
//        String username = jwtTokenProvider.getUsernameByToken(token);

        Map<String, String> map = new HashMap<>();
        map.put("eventName", dto.getEventName());
        map.put("regId", "");
        // cookie 에서 token 으로 값 넣기
//        map.put("regId", username);
        map.put("dcb", dto.getDcb());

        eventMapper.insertEvent(map);
    }

    public Page<Event> getEventPage(String dcb, String eventName, int page, int pageSize) {
        return functionUtil.toPage(getEventList(dcb, eventName), page, pageSize);
    }

    public void deleteEvent(DeleteEventDto dto) {
        for (String eventName : dto.getEventNames()) {
            Map<String, String> map = new HashMap<>();
            map.put("eventName", eventName);
            map.put("dcb", dto.getDcb());

            eventMapper.deleteEvent(map);
        }
    }

    private List<Event> getEventList(String dcb, String eventName) {
        Map<String, String> map = new HashMap<>();
        map.put("eventName", eventName);
        map.put("dcb", dcb);

        return eventMapper.getEventList(map);
    }

    public void updateEvent(HttpServletRequest request, UpdateEventDto updateEventDto) {
        String token = cookieUtil.getAccessToken(request).getValue();
        String username = jwtTokenProvider.getUsernameByToken(token);

        Map<String, String> map = new HashMap<>();
        map.put("eventName", updateEventDto.getEventName());
        map.put("username", username);

        eventMapper.updateEvent(map);
    }
}
