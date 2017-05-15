package io.teiler.server.endpoints;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;
import io.teiler.server.util.enums.Currency;

public class GroupEndpointControllerTest extends BaseEndpointControllerTest {

    private static final String GROUP_URL = URL_VERSION + "groups";

    private static final String POST_GROUP_NAME = "Manamana";
    private static final String GET_GROUP_ID = "gettgrup";
    private static final String GET_GROUP_NAME = "GetGroup";
    private static final String PUT_GROUP_ID = "puttgrup";
    private static final String PUT_GROUP_NAME = "PutGroupABC";
    private static final String DELETE_GROUP_ID = "deltgrup";

    @Test
    public void testPostGroup() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", POST_GROUP_NAME);
        
        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
                .post(GROUP_URL)
            .then()
                .statusCode(200)
                .body("name", equalTo(POST_GROUP_NAME))
                .body("id", is(notNullValue()))
                .body("currency", is(notNullValue()))
                .body("update-time", is(notNullValue()))
                .body("create-time", is(notNullValue()));
    }

    @Test
    public void testGetGroup() {
        given()
            .when()
                .get(GROUP_URL + "/" + GET_GROUP_ID)
            .then()
                .statusCode(200)
                .body("id", equalTo(GET_GROUP_ID))
                .body("name", equalTo(GET_GROUP_NAME))
                .body("currency", equalTo(Currency.CHF.toString().toLowerCase()))
                .body("update-time", is(notNullValue()))
                .body("create-time", is(notNullValue()));
    }

    @Test
    public void testPutGroup() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", PUT_GROUP_NAME);
        requestBody.put("currency", Currency.EUR.toString().toLowerCase());
        
        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
                .put(GROUP_URL + "/" + PUT_GROUP_ID)
            .then()
                .statusCode(200)
                .body("id", equalTo(PUT_GROUP_ID))
                .body("name", equalTo(PUT_GROUP_NAME))
                .body("currency", equalTo(Currency.EUR.toString().toLowerCase()))
                .body("update-time", is(notNullValue()))
                .body("create-time", is(notNullValue()));
    }
    
    @Test
    public void testDeleteGroup() {
        given()
            .when()
                .delete(GROUP_URL + "/" + DELETE_GROUP_ID)
            .then()
                .statusCode(200);
    }

}
