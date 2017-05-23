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
    private static final String INVALID_CURRENCY_GROUP_NAME = "InvalidCurrency";
    private static final String INVALID_CURRENCY = "XYZ";

    private static final String PARAM_NAME = "name";
    private static final String PARAM_ID = "id";
    private static final String PARAM_CURRENCY = "currency";
    private static final String PARAM_UPDATE_TIME = "update-time";
    private static final String PARAM_CREATE_TIME = "create-time";

    @Test
    public void testPostGroup() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put(PARAM_NAME, POST_GROUP_NAME);
        
        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
                .post(GROUP_URL)
            .then()
                .statusCode(200)
                .body(PARAM_NAME, equalTo(POST_GROUP_NAME))
                .body(PARAM_ID, is(notNullValue()))
                .body(PARAM_CURRENCY, is(notNullValue()))
                .body(PARAM_UPDATE_TIME, is(notNullValue()))
                .body(PARAM_CREATE_TIME, is(notNullValue()));
    }

    @Test
    public void testGetGroup() {
        given()
            .when()
                .get(GROUP_URL + "/" + GET_GROUP_ID)
            .then()
                .statusCode(200)
                .body(PARAM_ID, equalTo(GET_GROUP_ID))
                .body(PARAM_NAME, equalTo(GET_GROUP_NAME))
                .body(PARAM_CURRENCY, equalTo(Currency.CHF.toString().toLowerCase()))
                .body(PARAM_UPDATE_TIME, is(notNullValue()))
                .body(PARAM_CREATE_TIME, is(notNullValue()));
    }

    @Test
    public void testPutGroup() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put(PARAM_NAME, PUT_GROUP_NAME);
        requestBody.put(PARAM_CURRENCY, Currency.EUR.toString().toLowerCase());
        
        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
                .put(GROUP_URL + "/" + PUT_GROUP_ID)
            .then()
                .statusCode(200)
                .body(PARAM_ID, equalTo(PUT_GROUP_ID))
                .body(PARAM_NAME, equalTo(PUT_GROUP_NAME))
                .body(PARAM_CURRENCY, equalTo(Currency.EUR.toString().toLowerCase()))
                .body(PARAM_UPDATE_TIME, is(notNullValue()))
                .body(PARAM_CREATE_TIME, is(notNullValue()));
    }

    @Test
    public void testDeleteGroup() {
        given()
            .when()
                .delete(GROUP_URL + "/" + DELETE_GROUP_ID)
            .then()
                .statusCode(200);
    }

    @Test
    public void testInvalidCurrency() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put(PARAM_NAME, INVALID_CURRENCY_GROUP_NAME);
        requestBody.put(PARAM_CURRENCY, INVALID_CURRENCY);
        
        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
                .put(GROUP_URL + "/" + PUT_GROUP_ID)
            .then()
                .statusCode(416);
    }

}
