package com.lucas.signpost.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageTest {

    @Test
    public void testShouldDeserialize() throws JsonProcessingException {
        String message ="{\"id\": \"61d1559ae9764e455d920b0f\", \"message\": \"Hello World!\", \"user\": \"123\", \"location\": { \"latitude\": 51.65171598274609, \"longitude\": -3.241569174443418}}";
        ObjectMapper mapper = new ObjectMapper();
        Message actual = mapper.readValue(message, Message.class);
        assertEquals("61d1559ae9764e455d920b0f", actual.getId());
        assertEquals("Hello World!", actual.getMessage());
        assertEquals("123", actual.getUser());
        assertEquals(Double.valueOf(51.65171598274609), actual.getLocation().getLatitude());
        assertEquals(Double.valueOf(-3.241569174443418), actual.getLocation().getLongitude());
    }

}
