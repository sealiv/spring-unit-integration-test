package org.aleks4ay.repo;

import org.aleks4ay.dto.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Test for all repository method in 'EventRepo.class'")
class EventRepoIntegrationTest {

    private Event event;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EventRepo eventRepo;

    @BeforeEach
    public void init(){
        event = new Event("title_repo", "place_repo", "Anatoly", "Lecture_repo",
                LocalDateTime.of(2022, 5 , 5, 17, 17 ,17));
    }

    @Test
    void getEventTest() {
        jdbcTemplate.update("INSERT INTO event (id, title, place, speaker, event_type, date_time) values(? ,?, ?, ?, ?, ?);",
                8L, "title_8", "place for jdbcTemplate", "Anatoly", "Lecture", LocalDateTime.now());
        Event eventFromDB = eventRepo.findById(8L).orElse(null);
        assertNotNull(eventFromDB);
    }

    @Test
    void getAllEventsTest() {
        jdbcTemplate.update("INSERT INTO event (id, title, place, speaker, event_type, date_time) values(? ,?, ?, ?, ?, ?);",
                1L, "title_1", "place for jdbcTemplate", "Anatoly", "Lecture", LocalDateTime.now());
        jdbcTemplate.update("INSERT INTO event (id, title, place, speaker, event_type, date_time) values(? ,?, ?, ?, ?, ?);",
                2L, "title_2", "place for jdbcTemplate", "Anatoly", "Lecture", LocalDateTime.now());
        List<Event> eventsFromDB = new ArrayList<>();
        eventRepo.findAll().forEach(eventsFromDB::add);

        assertAll(()-> assertEquals("title_1", eventsFromDB.get(0).getTitle()),
                  ()-> assertEquals(2, eventsFromDB.size()));
    }

    @Test
    void getAllEventsByTitleTest() {
        jdbcTemplate.update("INSERT INTO event (id, title, place, speaker, event_type, date_time) values(? ,?, ?, ?, ?, ?);",
                1L, "title 11", "place for jdbcTemplate", "Anatoly", "Lecture", LocalDateTime.now());
        List<Event> foundEvent = eventRepo.getAllEventsByTitle("title 11");
        assertEquals("place for jdbcTemplate", foundEvent.get(0).getPlace());
    }

    @Test
    void createEventTest() {
        eventRepo.save(event);
        Event eventFromDB = jdbcTemplate.queryForObject("SELECT * FROM event WHERE title = ?;",
                new BeanPropertyRowMapper<>(Event.class), event.getTitle());
        assertNotNull(eventFromDB);
    }

    @Test
    @Sql(scripts = {"classpath:initTest.sql"})
    void deleteEventTest() {
        Event eventFromDBBeforeDeleting = jdbcTemplate.queryForObject("SELECT * FROM event WHERE title = ?;",
                new BeanPropertyRowMapper<>(Event.class), "title 5");
        assert eventFromDBBeforeDeleting != null : "Event didn't exist in DB before deleting";
        long deletingEventId = eventFromDBBeforeDeleting.getId();

        eventRepo.delete(eventFromDBBeforeDeleting);
        eventRepo.findAll();

        List<Event> eventFromDBAfterDeleting = jdbcTemplate.query("SELECT * FROM event;", new BeanPropertyRowMapper<>(Event.class));
        Event foundEvent = eventFromDBAfterDeleting.stream()
                .filter(event -> event.getId() == deletingEventId)
                .findFirst()
                .orElse(null);
        assertNull(foundEvent);
    }
}