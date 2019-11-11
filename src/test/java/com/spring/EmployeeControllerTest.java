package com.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.model.Event;
import com.spring.model.EventResponse;
import com.spring.model.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Random;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    MockMvc mvc;

    @Test
    public void testForValidateEvent() throws Exception {
        // threshold is 27
        Event e = new Event();
        e.setSensorId("fd0a635d-2aaf-4460-a817-6353e1b6c050");
        e.setEventId(UUID.randomUUID().toString());
        e.setTimestamp(1234567890);
        e.setValue(26d);
        MvcResult mvcResult = getMvcResult(mvc, e);
        doAssert(mvcResult, Status.NO_ANOMALY);

        // value is same as threshold 27
        e.setValue(27d);
        mvcResult = getMvcResult(mvc, e);
        doAssert(mvcResult, Status.NO_ANOMALY);

        // value is greater than threshold 27
        e.setValue(28d);
        mvcResult = getMvcResult(mvc, e);
        doAssert(mvcResult, Status.ANOMALY);

        // invalid sensor id
        e.setSensorId("fd0a635d-2aaf-4460-a817-6353e1b6c051");
        mvcResult = getMvcResult(mvc, e);
        doAssert(mvcResult, Status.NO_MODEL);
    }

    private MvcResult getMvcResult(MockMvc mvc, Event e) throws Exception {
        return mvc.perform(post("/events/").
                content(objectMapper.writeValueAsString(e))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }

    public void doAssert(MvcResult mvcResult, Status status) throws Exception {
        String response = mvcResult.getResponse().getContentAsString();
        EventResponse eventResponse = objectMapper.readValue(response, EventResponse.class);
        Assertions.assertEquals(status, eventResponse.getStatus());
    }
}
