package org.aleks4ay.repo;

import org.aleks4ay.dto.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@DisplayName("Test for unique repository method in 'EventRepo.class'")
public class EventRepoIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EventRepo eventRepo;

    @Test
    public void getAllEventsByTitle() {
        Event newEvent = new Event("title 11", "place one", "Anatoly", "Lecture",
                LocalDateTime.of(2022, 1 , 1, 12, 0 ,1));
        entityManager.persist(newEvent);
        entityManager.flush();

        List<Event> foundEvent = eventRepo.getAllEventsByTitle("title 11");

        assertThat(foundEvent.get(0).getTitle(), is(equalTo("title 11")));
    }
}