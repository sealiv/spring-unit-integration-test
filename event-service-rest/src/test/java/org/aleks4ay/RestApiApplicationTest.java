package org.aleks4ay;

import org.aleks4ay.rest.EventServiceController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Test that the whole application starts successfully")
class RestApiApplicationTest {

    @Autowired
    private EventServiceController controller;

    @Test
    public void contextLoads() {
        assertThat(controller).isNotNull();
    }
}