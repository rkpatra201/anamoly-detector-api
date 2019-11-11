package com.spring.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.builder.EventResponseBuilder;
import com.spring.model.Event;
import com.spring.model.EventConfig;
import com.spring.model.EventResponse;
import com.spring.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class EventService {

    private Map<String, EventConfig> eventConfigMap;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        loadEventConfig();
    }

    public EventResponse validateEvent(Event inputEvent) {
        String sensorId = inputEvent.getSensorId();
        Status status = null;
        if (!eventConfigMap.containsKey(sensorId)) {
            status = Status.NO_MODEL;
        } else {
            status = evaluateEventStatus(inputEvent);
        }
        return new EventResponseBuilder(inputEvent, status).build();
    }

    private Status evaluateEventStatus(Event inputEvent) {
        double value = inputEvent.getValue();
        EventConfig eventConfig = eventConfigMap.get(inputEvent.getSensorId());
        double threshold = eventConfig.getThreshold();
        int result = Double.compare(value, threshold);
        if (result == -1 || result == 0)
            return Status.NO_ANOMALY;
        else if (result == 1)
            return Status.ANOMALY;
        else
            return Status.ERROR;
    }

    private void loadEventConfig() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("event-config.json");
        TypeReference<HashMap<String, EventConfig>> typeRef
                = new TypeReference<HashMap<String, EventConfig>>() {
        };
        try {
            eventConfigMap = objectMapper.readValue(is, typeRef);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Collections.unmodifiableMap(eventConfigMap);
    }
}
