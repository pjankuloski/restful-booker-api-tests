package com.qa.restfulbooker.tests;

import com.qa.restfulbooker.base.BaseTest;
import com.qa.restfulbooker.clients.BookingClient;
import com.qa.restfulbooker.models.Booking;
import com.qa.restfulbooker.models.BookingDates;
import com.qa.restfulbooker.models.BookingResponse;
import com.qa.restfulbooker.utils.TestDataFactory;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UpdateBookingTests extends BaseTest {

    private final BookingClient bookingClient = new BookingClient();
    private int bookingId;

    @BeforeClass(alwaysRun = true)
    public void setupTestData() {
        Booking booking = TestDataFactory.randomBooking();
        Response response = bookingClient.createBooking(booking);
        bookingId = response.as(BookingResponse.class).getBookingid();
    }

    @Test(description = "PUT /booking/:id with a valid token fully replaces the booking")
    public void testUpdateBookingWithValidToken() {
        Booking updatedBooking = new Booking("James", "Updated", 500, true,
                new BookingDates("2026-08-01", "2026-08-10"), "Breakfast, WiFi");

        Response response = bookingClient.updateBooking(bookingId, updatedBooking, authToken);
        Assert.assertEquals(response.getStatusCode(), 200);

        Booking result = response.as(Booking.class);
        Assert.assertEquals(result.getFirstname(), "James");
        Assert.assertEquals(result.getLastname(), "Updated");
        Assert.assertEquals(result.getTotalprice(), Integer.valueOf(500));
        Assert.assertEquals(result.getAdditionalneeds(), "Breakfast, WiFi");
    }

    @Test(description = "PUT /booking/:id without a valid token is rejected with 403")
    public void testUpdateBookingWithoutToken() {
        Booking updatedBooking = TestDataFactory.randomBooking();
        Response response = bookingClient.updateBooking(bookingId, updatedBooking, "invalid-token");
        Assert.assertEquals(response.getStatusCode(), 403);
    }
}
