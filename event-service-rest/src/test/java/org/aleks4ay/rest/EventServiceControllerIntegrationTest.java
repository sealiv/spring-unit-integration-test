package org.aleks4ay.rest;

import org.aleks4ay.RestApiApplication;
import org.aleks4ay.dto.Event;
import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RestApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:initTest.sql"})
@DisplayName("Integration test for controller 'EventServiceController' with use @Sql script 'initTest.sql'")
public class EventServiceControllerIntegrationTest {

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
	public void getAllEventsTest() {
		String response = restTemplate.getForObject(createURLWithPort("/events"), String.class);
		assert(response != null);
		assertTrue(response.contains("[{\"id\":1,\"title\":\"title 4\",\"place\":\"place test\",\"speaker\":\"Anatoly\""));
	}

	@Test
	public void getEventTest() throws JSONException {

		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/events/1"),
				HttpMethod.GET, null, String.class);

		String expected = "{\"id\":1,\"title\":\"title 4\",\"place\":\"place test\",\"speaker\":\"Anatoly\",\"" +
				"eventType\":\"Lecture\",\"dateTime\":\"2022-06-02T12:00:01\"}";

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void createEventTest() {
		event.setTitle("title_createEvent");

		HttpEntity<Event> entity = new HttpEntity<>(event, null);

		restTemplate.exchange(createURLWithPort("/events"), HttpMethod.POST, entity, String.class);

		assertEquals("title_createEvent", eventServiceController.getEvent(4).getTitle());
	}

	@Test
	public void updateEventTest() {
		event.setTitle("title_updatedEvent");
		event.setId(3);

		HttpEntity<Event> entity = new HttpEntity<>(event, null);

		restTemplate.exchange(createURLWithPort("/events"), HttpMethod.PUT, entity, String.class);

		assertEquals("title_updatedEvent", eventServiceController.getEvent(3).getTitle());
	}

	@Test
	public void getAllEventsByTitleTest() throws JSONException {

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
