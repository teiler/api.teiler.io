package io.teiler.server.endpoints;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.teiler.server.Tylr;
import spark.Spark;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Tylr.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("integration-test")
public abstract class BaseEndpointControllerTest {

    private static final int SERVER_PORT = 4567;
    protected static final String URL_VERSION = GlobalEndpointController.URL_VERSION;
    
    @BeforeClass
    public static void beforeClass() {
        RestAssured.port = SERVER_PORT;
    }

    @AfterClass
    public static void afterClass() {
        Spark.stop();
    }
    
}
