package org.aleks4ay.service;

import org.aleks4ay.dto.Event;
import org.aleks4ay.repo.EventRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit test for service 'EventService' with use Mockito.mock(EventRepo.class)")
class EventServiceUnitTest {

    @Captor
    ArgumentCaptor<Event> asEvent;

    private Event event;
    EventRepo mockEventRepo = mock(EventRepo.class);

    @InjectMocks
    private EventServiceImpl eventService;

    @BeforeEach
    public void init(){
        event = new Event("title 12", "place one", "Anatoly", "Lecture",
                LocalDateTime.of(2022, 1 , 1, 12, 0 ,1));
    }

    @Test
    void createEventTest() {
        eventService.createEvent(event);
        verify(mockEventRepo, times(1)).save(asEvent.capture());
        assertEquals(event, asEvent.getValue());
    }

    @Test
    void deleteEventTest() {
        when(mockEventRepo.findById(anyLong())).thenReturn(Optional.of(event));
        eventService.deleteEvent(anyLong());
        verify(mockEventRepo, times(1)).delete(any(Event.class));
    }

    @Test
    void updateEventTest() {
        eventService.createEvent(event);
        verify(mockEventRepo, times(1)).save(event);
    }


    @Test
    void getEventTest() {
        when(mockEventRepo.findById(anyLong())).thenReturn(Optional.of(event));
        Event foundEvent = eventService.getEvent(1L);
        assertEquals("title 12", foundEvent.getTitle());
    }


    @Test
    void getAllEventsTest() {
        Event event1 = new Event("title 12", "place one", "Anatoly", "Lecture",
                LocalDateTime.of(2022, 1 , 1, 12, 0 ,1));
        Event event2 = new Event("title 13", "place one", "Anatoly", "Lecture",
                LocalDateTime.of(2022, 1 , 1, 12, 0 ,1));

        List<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);
        when(mockEventRepo.findAll()).thenReturn(events);

        List<Event> eventsFromDB = eventService.getAllEvents();

        assertEquals(2, eventsFromDB.size());
    }

    @Test
    void getAllEventsByTitleTest() {
        Event event1 = new Event("title 12", "place one", "Anatoly", "Lecture",
                LocalDateTime.of(2022, 1 , 1, 12, 0 ,1));
        Event event2 = new Event("title 12", "place one", "Anatoly", "Lecture",
                LocalDateTime.of(2022, 1 , 1, 12, 0 ,1));

        List<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);
        when(mockEventRepo.getAllEventsByTitle(anyString())).thenReturn(events);

        List<Event> eventsFromDB = eventService.getAllEventsByTitle("");

        assertEquals(2, eventsFromDB.size());
    }
}