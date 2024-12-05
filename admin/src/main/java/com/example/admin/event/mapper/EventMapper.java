package com.example.admin.event.mapper;

import com.example.admin.event.dto.Event;
import com.example.admin.event.dto.InsertEventDto;
import com.example.admin.event.dto.UpdateEventDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EventMapper {
    boolean insertEvent(InsertEventDto dto);
    List<Event> getEventList(Map<String, String> map);
    boolean updateEvent(UpdateEventDto dto);
    boolean deleteEvent(List<String> eventNames);
}
