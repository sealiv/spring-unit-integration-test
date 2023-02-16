package org.aleks4ay.rest;

import org.aleks4ay.RestApiApplication;
import org.aleks4ay.dto.Event;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = RestApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:initTest.sql"})
@DisplayName("Integration test for controller 'EventServiceController' with use @Sql script 'initTest.sql'")
class EventServiceControllerIntegrationTest {

	@Autowired
	EventServiceController eventServiceController;

	private Event event;

	@LocalServerPort
	private int port;

	RestTemplate restTemplate = new RestTemplate();

	@BeforeEach
	void setUp() {
		event = new Event("title controller", "place test", "Anatoly", "Lecture",
				LocalDateTime.of(2022, 6 , 2, 12, 0 ,1));
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}


	@Test
	void getAllEventsTest() {
		String response = restTemplate.getForObject(createURLWithPort("/events"), String.class);
		assert(response != null);
		assertTrue(response.contains("[{\"id\":1,\"title\":\"title 4\",\"place\":\"place test\",\"speaker\":\"Anatoly\""));
	}

	@Test
	void getEventTest() throws JSONException {

		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/events/1"),
				HttpMethod.GET, null, String.class);

		String expected = "{\"id\":1,\"title\":\"title 4\",\"place\":\"place test\",\"speaker\":\"Anatoly\",\"" +
				"eventType\":\"Lecture\",\"dateTime\":\"2022-06-02T12:00:01\"}";

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	void createEventTest() {
		event.setTitle("title_createEvent");

		HttpEntity<Event> entity = new HttpEntity<>(event, null);

		restTemplate.exchange(createURLWithPort("/events"), HttpMethod.POST, entity, String.class);

		assertEquals("title_createEvent", eventServiceController.getEvent(4).getTitle());
	}

	@Test
	void updateEventTest() {
		event.setTitle("title_updatedEvent");
		event.setId(3);

		HttpEntity<Event> entity = new HttpEntity<>(event, null);

		restTemplate.exchange(createURLWithPort("/events"), HttpMethod.PUT, entity, String.class);

		assertEquals("title_updatedEvent", eventServiceController.getEvent(3).getTitle());
	}

	@Test
	void getAllEventsByTitleTest() throws JSONException {

		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/events/getByTitle?title=title 5"),
				HttpMethod.GET, null, String.class);

		String expected = "[{\"id\":3,\"title\":\"title 5\",\"place\":\"place test\",\"speaker\":\"Alexey\",\"" +
				"eventType\":\"Dance\",\"dateTime\":\"2022-06-04T12:00:01\"}]";

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}


	@Test
	void deleteEvent() {
		Event eventForDeleting = eventServiceController.getEvent(2);
		long confirmExistingId = eventForDeleting.getId();

		restTemplate.exchange(createURLWithPort("/events/2"), HttpMethod.DELETE, null, String.class);

		assertThrows(RuntimeException.class, ()-> eventServiceController.getEvent(confirmExistingId));
	}
}
