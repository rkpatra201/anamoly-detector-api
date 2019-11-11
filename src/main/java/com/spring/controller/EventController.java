package com.spring.controller;

import com.spring.model.Event;
import com.spring.model.EventResponse;
import com.spring.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/")
    public ResponseEntity<Event> getSampleEvent()
    {
        Event e = new Event();
        e.setValue(25.674);
        return ResponseEntity.status(HttpStatus.OK).body(e);
    }
    @PostMapping("/")
    public ResponseEntity<EventResponse> validateEvent(@RequestBody Event event)
    {
        EventResponse eventResponse = eventService.validateEvent(event);
        return ResponseEntity.status(HttpStatus.OK).body(eventResponse);
    }
}
