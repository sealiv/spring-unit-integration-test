package org.aleks4ay.service;

import org.aleks4ay.api.EventService;
import org.aleks4ay.dto.Event;
import org.aleks4ay.repo.EventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepo eventRepo;

    @Autowired
    public EventServiceImpl(EventRepo eventRepo) {
        this.eventRepo = eventRepo;
    }

    @Override
    public Event createEvent(Event event) {
        return eventRepo.save(event);
    }

    @Override
    public void updateEvent(Event event) {
        eventRepo.save(event);
    }

    @Override
    public Event getEvent(long id) {
        return eventRepo.findById(id).orElseThrow(()-> new RuntimeException("No Event with id='" + id + "'."));
    }

    @Override
    public void deleteEvent(long id) {
        Event event = getEvent(id);
        if (event != null) {
            eventRepo.delete(event);
        }
    }

    @Override
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        eventRepo.findAll().forEach(events::add);
        return events;
    }

    @Override
    public List<Event> getAllEventsByTitle(String title) {
        return eventRepo.getAllEventsByTitle(title);
    }
}
