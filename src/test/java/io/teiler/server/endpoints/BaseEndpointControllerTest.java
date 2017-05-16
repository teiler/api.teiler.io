package io.teiler.server.endpoints;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
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

    private static final int SERVER_PORT = 4567;
    protected static final String URL_VERSION = GlobalEndpointController.URL_VERSION;
    
    @BeforeClass
    public static void beforeClass() {
        /**
         * We need to stop Spark manually before running any of the integration tests.
         * Otherwise we'll get tons of Exceptions. */
        Spark.stop();
        
        RestAssured.port = SERVER_PORT;
    }
    
    @Before
    public void before() {
        Spark.awaitInitialization();
    }

    @AfterClass
    public static void afterClass() {
        Spark.stop();
    }
    
}
