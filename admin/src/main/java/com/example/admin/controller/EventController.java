package com.example.admin.controller;

import com.example.admin.domain.dto.event.DeleteEventDto;
import com.example.admin.domain.dto.event.InsertEventDto;
import com.example.admin.domain.entity.event.Event;
import com.example.admin.service.event.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;

    @PostMapping("/gdcb")
    public void insertEvent(@RequestBody @Valid InsertEventDto dto) {
        eventService.insertEvent(dto);
    }

    @GetMapping("/gdcb")
    public Page<Event> getEventPage(@RequestParam("dcb") @Valid String dcb,
                                    @RequestParam("eventName") @Valid String eventName,
                                    @RequestParam("page") @Valid int page,
                                    @RequestParam("pageSize") @Valid int pageSize) {
        return eventService.getEventPage(dcb, eventName, page, pageSize);
    }

    @DeleteMapping("/gdcb")
    public void deletePage(DeleteEventDto dto) {
        eventService.deleteEvent(dto);
    }
}
