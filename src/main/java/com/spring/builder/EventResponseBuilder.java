package com.spring.builder;

import com.spring.model.Event;
import com.spring.model.EventResponse;
import com.spring.model.Status;
import org.springframework.beans.BeanUtils;

public class EventResponseBuilder {

    private Event event;
    private Status status;
    public EventResponseBuilder(Event inputEvent, Status status)
    {
        this.event = inputEvent;
        this.status = status;
    }

    public EventResponse build()
    {
        EventResponse eventResponse = new EventResponse();
        BeanUtils.copyProperties(event, eventResponse);
        eventResponse.setStatus(this.status);
        eventResponse.setCause(this.status.getCause());
        eventResponse.setMessage(this.status.getMessage());
        return eventResponse;
    }
}
