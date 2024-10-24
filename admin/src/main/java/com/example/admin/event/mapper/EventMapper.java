package com.example.admin.event.mapper;

import com.example.admin.event.dto.Event;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EventMapper {
    void insertEvent(Map<String, String> map);
    List<Event> getEventList(Map<String, String> map);
    void updateEvent(Map<String, String> map);
    void deleteEvent(Map<String, String> map);
}
