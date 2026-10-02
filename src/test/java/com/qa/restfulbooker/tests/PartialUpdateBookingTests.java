package com.qa.restfulbooker.tests;

import com.qa.restfulbooker.base.BaseTest;
import com.qa.restfulbooker.clients.BookingClient;
import com.qa.restfulbooker.models.Booking;
import com.qa.restfulbooker.models.BookingResponse;
import com.qa.restfulbooker.utils.TestDataFactory;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class PartialUpdateBookingTests extends BaseTest {

    private final BookingClient bookingClient = new BookingClient();
    private int bookingId;
    private Booking originalBooking;

    @BeforeClass(alwaysRun = true)
    public void setupTestData() {
        originalBooking = TestDataFactory.randomBooking();
        Response response = bookingClient.createBooking(originalBooking);
        bookingId = response.as(BookingResponse.class).getBookingid();
    }

    @Test(description = "PATCH /booking/:id updates only the supplied fields and leaves the rest untouched")
    public void testPartialUpdateBooking() {
        Map<String, String> partialPayload = new HashMap<>();
        partialPayload.put("firstname", "Patched");
        partialPayload.put("lastname", "Guest");

        Response response = bookingClient.partialUpdateBooking(bookingId, partialPayload, authToken);
        Assert.assertEquals(response.getStatusCode(), 200);

        Booking result = response.as(Booking.class);
        Assert.assertEquals(result.getFirstname(), "Patched");
        Assert.assertEquals(result.getLastname(), "Guest");
        // Fields that were not part of the PATCH body should be unchanged
        Assert.assertEquals(result.getTotalprice(), originalBooking.getTotalprice());
        Assert.assertEquals(result.getDepositpaid(), originalBooking.getDepositpaid());
    }

    @Test(description = "PATCH /booking/:id without a valid token is rejected with 403")
    public void testPartialUpdateBookingWithoutToken() {
        Map<String, String> partialPayload = new HashMap<>();
        partialPayload.put("firstname", "ShouldNotApply");

        Response response = bookingClient.partialUpdateBooking(bookingId, partialPayload, "invalid-token");
        Assert.assertEquals(response.getStatusCode(), 403);
    }
}
