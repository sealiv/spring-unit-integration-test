package org.aleks4ay.rest;

import org.aleks4ay.dto.Event;
import org.aleks4ay.service.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/events")
public class EventServiceController {

    @Autowired
    private EventServiceImpl eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createEvent(@RequestBody @Validated Event event) {
        eventService.createEvent(event);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Event getEvent(@PathVariable(value = "id") long id) {
        return eventService.getEvent(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping(path = "/getByTitle", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Event> getAllEventsByTitle(@PathParam(value = "title") String title) {
        return eventService.getAllEventsByTitle(title);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteEvent(@PathVariable(value = "id") long id) {
        eventService.deleteEvent(id);
    }

    @PutMapping
    public void updateEvent(@RequestBody @Validated Event event) {
        if (eventService.getEvent(event.getId()) != null) {
            eventService.updateEvent(event);
        } else {
            throw new NoSuchElementException("Event with id = '" + event.getId() + "' not found.");
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return400(NoSuchElementException ex) {
        return ex.getMessage();
    }
}
