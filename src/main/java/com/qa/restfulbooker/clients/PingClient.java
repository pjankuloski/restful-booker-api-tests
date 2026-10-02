package com.qa.restfulbooker.clients;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PingClient {

    public Response healthCheck() {
        return given()
                .when()
                .get("/ping")
                .then()
                .extract()
                .response();
    }
}
