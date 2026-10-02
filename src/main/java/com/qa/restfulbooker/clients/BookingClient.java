package com.qa.restfulbooker.clients;

import com.qa.restfulbooker.models.Booking;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Service-object layer for every /booking endpoint documented for restful-booker:
 * GetBookingIds, GetBooking, CreateBooking, UpdateBooking, PartialUpdateBooking, DeleteBooking.
 */
public class BookingClient {

    private static final String BASE_PATH = "/booking";

    public Response getBookingIds() {
        return given()
                .when()
                .get(BASE_PATH)
                .then()
                .extract()
                .response();
    }

    public Response getBookingIdsByParams(Map<String, Object> queryParams) {
        return given()
                .queryParams(queryParams)
                .when()
                .get(BASE_PATH)
                .then()
                .extract()
                .response();
    }

    public Response getBookingById(int id) {
        return given()
                .accept("application/json")
                .when()
                .get(BASE_PATH + "/" + id)
                .then()
                .extract()
                .response();
    }

    public Response createBooking(Booking booking) {
        return given()
                .contentType("application/json")
                .accept("application/json")
                .body(booking)
                .when()
                .post(BASE_PATH)
                .then()
                .extract()
                .response();
    }

    public Response updateBooking(int id, Booking booking, String token) {
        return given()
                .contentType("application/json")
                .accept("application/json")
                .header(new Header("Cookie", "token=" + token))
                .body(booking)
                .when()
                .put(BASE_PATH + "/" + id)
                .then()
                .extract()
                .response();
    }

    public Response partialUpdateBooking(int id, Object partialBody, String token) {
        return given()
                .contentType("application/json")
                .accept("application/json")
                .header(new Header("Cookie", "token=" + token))
                .body(partialBody)
                .when()
                .patch(BASE_PATH + "/" + id)
                .then()
                .extract()
                .response();
    }

    public Response deleteBooking(int id, String token) {
        return given()
                .header(new Header("Cookie", "token=" + token))
                .when()
                .delete(BASE_PATH + "/" + id)
                .then()
                .extract()
                .response();
    }

    public Response deleteBookingNoAuth(int id) {
        return given()
                .when()
                .delete(BASE_PATH + "/" + id)
                .then()
                .extract()
                .response();
    }
}
