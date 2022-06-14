package org.aleks4ay;

import org.aleks4ay.repo.EventRepoIntegrationTest;
import org.aleks4ay.rest.EventServiceControllerIntegrationTest;
import org.aleks4ay.rest.EventServiceControllerUnitTest;
import org.aleks4ay.service.EventServiceIntegrationTest;
import org.aleks4ay.service.EventServiceUnitTest;
import org.junit.platform.suite.api.*;

@Suite
@SelectClasses( {RestApiApplicationTest.class,
        EventRepoIntegrationTest.class,
        EventServiceUnitTest.class, EventServiceIntegrationTest.class,
        EventServiceControllerUnitTest.class, EventServiceControllerIntegrationTest.class
} )
@SuiteDisplayName("A Test Suite for 'Event'")
public class TestSuite {
}
