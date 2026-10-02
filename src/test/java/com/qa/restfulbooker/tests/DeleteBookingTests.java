package com.qa.restfulbooker.tests;

import com.qa.restfulbooker.base.BaseTest;
import com.qa.restfulbooker.clients.BookingClient;
import com.qa.restfulbooker.models.Booking;
import com.qa.restfulbooker.models.BookingResponse;
import com.qa.restfulbooker.utils.TestDataFactory;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DeleteBookingTests extends BaseTest {

    private final BookingClient bookingClient = new BookingClient();
    private int bookingId;

    // A fresh booking per test method: DELETE is destructive, so each test
    // gets its own disposable record instead of sharing one across methods.
    @BeforeMethod(alwaysRun = true)
    public void createFreshBooking() {
        Booking booking = TestDataFactory.randomBooking();
        Response response = bookingClient.createBooking(booking);
        bookingId = response.as(BookingResponse.class).getBookingid();
    }

    @Test(description = "DELETE /booking/:id without a token is rejected with 403")
    public void testDeleteBookingWithoutToken() {
        Response response = bookingClient.deleteBookingNoAuth(bookingId);
        Assert.assertEquals(response.getStatusCode(), 403);
    }

    @Test(description = "DELETE /booking/:id with a valid token returns 201 and removes the booking")
    public void testDeleteBookingWithValidToken() {
        Response deleteResponse = bookingClient.deleteBooking(bookingId, authToken);
        Assert.assertEquals(deleteResponse.getStatusCode(), 201);

        Response getResponse = bookingClient.getBookingById(bookingId);
        Assert.assertEquals(getResponse.getStatusCode(), 404, "Deleted booking should no longer exist");
    }
}
