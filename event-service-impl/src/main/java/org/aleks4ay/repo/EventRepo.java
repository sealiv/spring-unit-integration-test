package org.aleks4ay.repo;

import org.aleks4ay.dto.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepo extends CrudRepository<Event, Long> {
    List<Event> getAllEventsByTitle(String title);
}
