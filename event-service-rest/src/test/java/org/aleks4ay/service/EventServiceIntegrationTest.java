package org.aleks4ay.service;

import org.aleks4ay.dto.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(scripts = {"classpath:initTest.sql"})
class EventServiceIntegrationTest {

    private Event event;

    @Autowired
    private EventServiceImpl eventService;

    @BeforeEach
    public void init(){
        event = new Event("title 4", "place test", "Anatoly", "Lecture",
                LocalDateTime.of(2022, 6 , 2, 12, 0 ,1));
    }

    @Test
    public void createEventTest() {
        Event eventAfterSaving = eventService.createEvent(event);
        assertTrue(eventAfterSaving.getId() > 0);
    }

    @Test
    public void deleteEventTest() {
        Event findEvent = eventService.getEvent(2L);
        eventService.deleteEvent(findEvent.getId());
        assertThrows(RuntimeException.class, ()-> eventService.getEvent(findEvent.getId()));
    }

    @Test
    public void updateEventTest() {
        event.setTitle("updated title");
        eventService.updateEvent(event);
        Event eventAfterSaving = eventService.getEvent(event.getId());
        assertEquals(event.getTitle(), eventAfterSaving.getTitle());
    }

    @Test
    public void getEventTest() {
        event.setId(1L);
        Event eventFromDB = eventService.getEvent(1L);
        assertEquals(event.toString(), eventFromDB.toString());
    }

    @Test
    public void getAllEventTest() {
        List<Event> eventsFromDB = eventService.getAllEvents();

        assertAll(()-> assertEquals(3, eventsFromDB.size()),
                  ()-> assertEquals("title 5", eventsFromDB.get(2).getTitle()));
    }

    @Test
    public void getAllEventsByTitleTest() {
        List<Event> eventsFromDB = eventService.getAllEventsByTitle("title 4");
        System.out.println(eventsFromDB);
        assertAll(()-> assertEquals(2, eventsFromDB.size()),
                  ()-> assertEquals("title 4", eventsFromDB.get(0).getTitle()));
    }
}