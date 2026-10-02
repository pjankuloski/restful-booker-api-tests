package com.qa.restfulbooker.clients;

import com.qa.restfulbooker.models.AuthRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Thin wrapper around POST /auth. Returning the raw REST Assured {@link Response}
 * (instead of asserting inside the client) keeps assertions in the test layer,
 * where they belong.
 */
public class AuthClient {

    public Response createToken(AuthRequest authRequest) {
        return given()
                .contentType("application/json")
                .body(authRequest)
                .when()
                .post("/auth")
                .then()
                .extract()
                .response();
    }
}
