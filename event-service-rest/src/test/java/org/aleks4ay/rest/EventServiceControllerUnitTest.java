package org.aleks4ay.rest;

import org.aleks4ay.dto.Event;
import org.aleks4ay.service.EventServiceImpl;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventServiceController.class)
@DisplayName("Unit test for controller 'EventServiceController' with use MockMvc")
public class EventServiceControllerUnitTest {

    private Event event;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventServiceImpl eventService;

    @BeforeEach
    void setUp() {
        event = new Event("title controller", "place test", "Anatoly", "Lecture",
                LocalDateTime.of(2022, 6 , 2, 12, 0 ,1));
    }

    @Test
    void createEventTest() throws Exception{
        mockMvc.perform(post("/events")
                        .content(getJson(new Event()).toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        verify(eventService, times(1)).createEvent(any(Event.class));
    }

    @Test
    void getEventTest() throws Exception{
        when(eventService.getEvent(anyLong())).thenReturn(event);
        this.mockMvc.perform(get("/events/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("\"title\":\"title controller\"")));
    }

    @Test
    void getAllEventsTest() throws Exception {
        when(eventService.getAllEvents()).thenReturn(Collections.singletonList(event));
        this.mockMvc
                .perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("title controller")));
    }

    @Test
    void getAllEventsByTitle() throws Exception{
        when(eventService.getAllEventsByTitle("tit")).thenReturn(Collections.singletonList(event));
        this.mockMvc
                .perform(get("/events/getByTitle?title=tit"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("title controller")));
    }

    @Test
    void deleteEvent() throws Exception{
        when(eventService.getEvent(anyLong())).thenReturn(new Event());
        mockMvc.perform(delete("/events/1"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void updateEvent() throws Exception{
        when(eventService.getEvent(anyLong())).thenReturn(new Event());

        mockMvc.perform(put("/events")
                        .content(getJson(new Event()).toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }


    public static JSONObject getJson(Event event) throws Exception{
        return new JSONObject()
                .put("id", event.getId())
                .put("title", event.getTitle())
                .put("place", event.getPlace())
                .put("speaker", event.getSpeaker())
                .put("eventType", event.getEventType())
                .put("dateTime", event.getDateTime());
    }
}