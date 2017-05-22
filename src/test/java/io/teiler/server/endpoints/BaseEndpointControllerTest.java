package io.teiler.server.endpoints;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.teiler.server.Tylr;
import spark.Spark;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Tylr.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {"local.server.port=4567"})
@ActiveProfiles("integration-test")
public abstract class BaseEndpointControllerTest {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseEndpointControllerTest.class);
    
    private static final int SERVER_PORT = 4567;
    protected static final String URL_VERSION = GlobalEndpointController.URL_VERSION;
    
    private boolean sparkInitialized = false;
    
    @BeforeClass
    public static void beforeClass() {
        RestAssured.port = SERVER_PORT;
    }
    
    @Before
    public void before() {
        if (!sparkInitialized) {
            LOGGER.info("await initialization");
            Spark.awaitInitialization();
            sparkInitialized = true;
            LOGGER.info("initialization done");
        }
    }
    
}
