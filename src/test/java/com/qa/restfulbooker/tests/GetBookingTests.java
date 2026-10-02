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

public class GetBookingTests extends BaseTest {

    private final BookingClient bookingClient = new BookingClient();
    private int createdBookingId;
    private Booking createdBooking;

    @BeforeClass(alwaysRun = true)
    public void setupTestData() {
        // Use a fixed, recognisable name so the "filter by name" test has something
        // deterministic to look for, on top of the randomized fields.
        createdBooking = TestDataFactory.randomBooking("Michael", "Johnson");
        Response response = bookingClient.createBooking(createdBooking);
        createdBookingId = response.as(BookingResponse.class).getBookingid();
    }

    @Test(description = "GET /booking returns 200 and a non-empty array of booking ids")
    public void testGetAllBookingIds() {
        Response response = bookingClient.getBookingIds();

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.jsonPath().getList("$").size() > 0,
                "Booking id list should not be empty");
    }

    @Test(description = "GET /booking/:id with a valid id returns the correct booking data")
    public void testGetBookingByValidId() {
        Response response = bookingClient.getBookingById(createdBookingId);

        Assert.assertEquals(response.getStatusCode(), 200);
        Booking booking = response.as(Booking.class);
        Assert.assertEquals(booking.getFirstname(), createdBooking.getFirstname());
        Assert.assertEquals(booking.getLastname(), createdBooking.getLastname());
    }

    @Test(description = "GET /booking/:id with a non-existent id returns 404")
    public void testGetBookingByInvalidId() {
        Response response = bookingClient.getBookingById(999999999);
        Assert.assertEquals(response.getStatusCode(), 404);
    }

    @Test(description = "GET /booking?firstname=&lastname= filters results correctly")
    public void testGetBookingIdsFilteredByName() {
        Map<String, Object> params = new HashMap<>();
        params.put("firstname", createdBooking.getFirstname());
        params.put("lastname", createdBooking.getLastname());

        Response response = bookingClient.getBookingIdsByParams(params);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.jsonPath().getList("$").size() > 0,
                "Should find at least the booking created for this test class");
    }

    @Test(description = "GET /booking?checkin=&checkout= filters by date range without erroring")
    public void testGetBookingIdsFilteredByDates() {
        Map<String, Object> params = new HashMap<>();
        params.put("checkin", createdBooking.getBookingdates().getCheckin());
        params.put("checkout", createdBooking.getBookingdates().getCheckout());

        Response response = bookingClient.getBookingIdsByParams(params);
        Assert.assertEquals(response.getStatusCode(), 200);
    }
}
