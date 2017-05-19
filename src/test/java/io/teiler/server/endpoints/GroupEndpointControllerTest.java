/**
 * MIT License
 *
 * Copyright (c) 2017 L. Röllin, P. Bächli, K. Thurairatnam & D. Thoma
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

    private static final String POST_GROUP_NAME = "Manamana";
    private static final String GET_GROUP_ID = "gettgrup";
    private static final String GET_GROUP_NAME = "GetGroup";
    private static final String PUT_GROUP_ID = "puttgrup";
    private static final String PUT_GROUP_NAME = "PutGroupABC";
    private static final String DELETE_GROUP_ID = "deltgrup";
    
    private static final String PARAM_NAME = "name";
    private static final String PARAM_ID = "id";
    private static final String PARAM_CURRENCY = "currency";
    private static final String PARAM_UPDATE_TIME = "update-time";
    private static final String GROUP_URL = URL_VERSION + "groups";
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

}
