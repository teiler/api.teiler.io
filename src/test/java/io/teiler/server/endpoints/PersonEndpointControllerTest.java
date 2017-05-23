package io.teiler.server.endpoints;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;

public class PersonEndpointControllerTest extends BaseEndpointControllerTest {

    private static final String GROUP_ID = "persgrup";
    private static final String PERSON_URL = URL_VERSION + "groups/" + GROUP_ID + "/people";

    private static final String POST_PERSON_NAME = "Richi";
    private static final int PUT_PERSON_ID = 2;
    private static final String PUT_PERSON_NAME = "PutPersonABC";
    private static final int DELETE_PERSON_ID = 3;

    private static final String PEOPLE_GROUP_ID = "piplgrup";
    private static final String PERSON_PEOPLE_URL = URL_VERSION + "groups/" + PEOPLE_GROUP_ID + "/people";
    private static final int GET_PERSON_1_ID = 7;
    private static final String GET_PERSON_1_NAME = "PeoplePerson1";
    private static final int GET_PERSON_2_ID = 8;
    private static final String GET_PERSON_2_NAME = "PeoplePerson2";
    
    private static final int PERSON_NOT_FOUND_ID = 666;
    
    private static final String PEOPLE_NAME_CONFLICT_GROUP_ID = "spargrup";
    private static final String PEOPLE_NAME_CONFLICT_URL = URL_VERSION + "groups/" + PEOPLE_NAME_CONFLICT_GROUP_ID + "/people";    
    private static final String PEOPLE_NAME_CONFLICT_PERSON_NAME = "Spartacus";

    private static final String PARAM_NAME = "name";
    private static final String PARAM_ID = "id";
    private static final String PARAM_ACTIVE = "active";
    private static final String PARAM_UPDATE_TIME = "update-time";
    private static final String PARAM_CREATE_TIME = "create-time";

    @Test
    public void testPostPerson() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put(PARAM_NAME, POST_PERSON_NAME);
        
        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
                .post(PERSON_URL)
            .then()
                .statusCode(200)
                .body(PARAM_NAME, equalTo(POST_PERSON_NAME))
                .body(PARAM_ID, is(notNullValue()))
                .body(PARAM_ACTIVE, is(true))
                .body(PARAM_UPDATE_TIME, is(notNullValue()))
                .body(PARAM_CREATE_TIME, is(notNullValue()));
    }

    @Test
    public void testGetPeople() {
        given()
            .when()
                .get(PERSON_PEOPLE_URL)
            .then()
                .statusCode(200)
                .body(PARAM_ID, hasItems(GET_PERSON_1_ID, GET_PERSON_2_ID))
                .body(PARAM_NAME, hasItems(GET_PERSON_1_NAME, GET_PERSON_2_NAME))
                .body(PARAM_ACTIVE, hasItems(true, true))
                .body(PARAM_UPDATE_TIME, is(notNullValue()))
                .body(PARAM_CREATE_TIME, is(notNullValue()));
    }

    @Test
    public void testPutPerson() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put(PARAM_NAME, PUT_PERSON_NAME);
        
        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
                .put(PERSON_URL + "/" + PUT_PERSON_ID)
            .then()
                .statusCode(200)
                .body(PARAM_ID, equalTo(PUT_PERSON_ID))
                .body(PARAM_NAME, equalTo(PUT_PERSON_NAME))
                .body(PARAM_ACTIVE, is(true))
                .body(PARAM_UPDATE_TIME, is(notNullValue()))
                .body(PARAM_CREATE_TIME, is(notNullValue()));
    }

    @Test
    public void testDeletePerson() {
        given()
            .when()
                .delete(PERSON_URL + "/" + DELETE_PERSON_ID)
            .then()
                .statusCode(200);
    }
    
    @Test
    public void testPersonNotFound() {
        given()
            .when()
                .get(PERSON_URL + "/" + PERSON_NOT_FOUND_ID)
            .then()
                .statusCode(404);
    }
    
    @Test
    public void testPeopleNameConflict() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put(PARAM_NAME, PEOPLE_NAME_CONFLICT_PERSON_NAME);
        
        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
                .post(PEOPLE_NAME_CONFLICT_URL)
            .then()
                .statusCode(409);
    }

}
