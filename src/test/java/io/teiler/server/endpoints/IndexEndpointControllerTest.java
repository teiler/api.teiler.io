package io.teiler.server.endpoints;

import static io.restassured.RestAssured.get;

import org.junit.Test;

public class IndexEndpointControllerTest extends BaseEndpointControllerTest {
    
    @Test
    public void testIndexEndpoint() {
        get("").then().assertThat().statusCode(200);
    }

}
