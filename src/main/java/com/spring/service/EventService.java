package com.spring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.builder.EventResponseBuilder;
import com.spring.model.Event;
import com.spring.model.EventConfig;
import com.spring.model.EventResponse;
import com.spring.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);
    private Map<String, EventConfig> eventConfigMap;
    private double sumValue=0;
    private int eventCount = 0;
    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        loadEventConfig();
    }

    public EventResponse validateEvent(Event inputEvent) {
        LOGGER.debug("event received: "+convertEventToJson(inputEvent));
        String sensorId = inputEvent.getSensorId();
        Status status = null;
        if (!eventConfigMap.containsKey(sensorId)) {
            status = Status.NO_MODEL;
        } else {
            status = evaluateEventStatus(inputEvent);
        }
        return new EventResponseBuilder(inputEvent, status).build();
    }

    private synchronized Status evaluateEventStatus(Event inputEvent) {
        double value = inputEvent.getValue();
        EventConfig eventConfig = eventConfigMap.get(inputEvent.getSensorId());
        sumValue = value + sumValue;
        eventCount = eventCount + 1;
        double avgValue = sumValue /eventCount;
        double threshold = eventConfig.getThreshold();
        int result = Double.compare(avgValue, threshold);
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
    private String convertEventToJson(Event e)
    {
        try {
            return objectMapper.writeValueAsString(e);
        } catch (JsonProcessingException e1) {
            throw new RuntimeException(e1);
        }
    }
}
