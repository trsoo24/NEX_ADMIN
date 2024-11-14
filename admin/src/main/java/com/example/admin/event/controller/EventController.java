package com.example.admin.event.controller;

import com.example.admin.common.response.StatusResult;
import com.example.admin.event.dto.DeleteEventDto;
import com.example.admin.event.dto.InsertEventDto;
import com.example.admin.event.dto.UpdateEventDto;
import com.example.admin.event.dto.Event;
import com.example.admin.event.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;

    @PostMapping
    public StatusResult insertEvent(@RequestBody @Valid InsertEventDto dto) {
        eventService.insertEvent(dto);

        return new StatusResult(true);
    }

    @GetMapping
    public List<Event> getEventPage(@RequestParam("eventName") @Valid String eventName) {
        return eventService.getEventList(eventName);
    }

    @DeleteMapping
    public StatusResult deletePage(@RequestBody @Valid DeleteEventDto dto) {
        eventService.deleteEvent(dto);

        return new StatusResult(true);
    }

    @PutMapping
    public StatusResult updateEvent(@RequestBody @Valid UpdateEventDto dto) {
        eventService.updateEvent( dto);

        return new StatusResult(true);
    }
}
