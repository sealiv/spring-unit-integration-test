package org.aleks4ay.api;

import org.aleks4ay.dto.Event;

import java.util.List;

public interface EventService {
    Event createEvent(Event event);
    void updateEvent(Event event);
    Event getEvent(long id);
    void deleteEvent(long id);
    List<Event> getAllEvents();
    List<Event> getAllEventsByTitle(String title);
}
