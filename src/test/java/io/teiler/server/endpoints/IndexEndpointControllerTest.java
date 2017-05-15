package io.teiler.server.endpoints;

import static io.restassured.RestAssured.get;

public class IndexEndpointControllerTest extends BaseEndpointControllerTest {
    
//    @Test
    public void testIndexEndpoint() {
        get("").then().assertThat().statusCode(200);
    }

}
