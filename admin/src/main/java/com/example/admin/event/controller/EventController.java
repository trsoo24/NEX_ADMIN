package com.example.admin.event.controller;

import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.event.dto.DeleteEventDto;
import com.example.admin.event.dto.InsertEventDto;
import com.example.admin.event.dto.UpdateEventDto;
import com.example.admin.event.dto.Event;
import com.example.admin.event.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
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
    public StatusResult insertEvent(HttpServletRequest request, @RequestBody @Valid InsertEventDto dto) {
        eventService.insertEvent(request, dto);

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
    public StatusResult deletePage(@RequestBody @Valid DeleteEventDto dto) {
        eventService.deleteEvent(dto);

        return new StatusResult(true);
    }

    @PutMapping
    public StatusResult updateEvent(HttpServletRequest request, @RequestBody @Valid UpdateEventDto dto) {
        eventService.updateEvent(request, dto);

        return new StatusResult(true);
    }
}
