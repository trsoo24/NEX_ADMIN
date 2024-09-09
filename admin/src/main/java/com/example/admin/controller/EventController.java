package com.example.admin.controller;

import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
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
    public StatusResult insertEvent(@RequestBody @Valid InsertEventDto dto) {
        eventService.insertEvent(dto);

        return new StatusResult(true);
    }

    @GetMapping("/gdcb")
    public PageResult<Event> getEventPage(@RequestParam("dcb") @Valid String dcb,
                                          @RequestParam("eventName") @Valid String eventName,
                                          @RequestParam("page") @Valid int page,
                                          @RequestParam("pageSize") @Valid int pageSize) {
        Page<Event> eventPage = eventService.getEventPage(dcb, eventName, page, pageSize);

        return new PageResult<>(true, eventPage);
    }

    @DeleteMapping("/gdcb")
    public StatusResult deletePage(DeleteEventDto dto) {
        eventService.deleteEvent(dto);

        return new StatusResult(true);
    }
}
