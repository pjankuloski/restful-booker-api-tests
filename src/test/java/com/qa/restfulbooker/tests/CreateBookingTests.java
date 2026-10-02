package com.qa.restfulbooker.tests;

import com.qa.restfulbooker.base.BaseTest;
import com.qa.restfulbooker.clients.BookingClient;
import com.qa.restfulbooker.models.Booking;
import com.qa.restfulbooker.models.BookingDates;
import com.qa.restfulbooker.models.BookingResponse;
import com.qa.restfulbooker.utils.TestDataFactory;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CreateBookingTests extends BaseTest {

    private final BookingClient bookingClient = new BookingClient();

    @Test(description = "POST /booking with a full, valid payload returns a bookingid and echoes the data back")
    public void testCreateBookingWithValidData() {
        Booking booking = TestDataFactory.randomBooking();
        Response response = bookingClient.createBooking(booking);

        Assert.assertEquals(response.getStatusCode(), 200);
        BookingResponse bookingResponse = response.as(BookingResponse.class);

        Assert.assertTrue(bookingResponse.getBookingid() > 0, "Booking id should be generated");
        Assert.assertEquals(bookingResponse.getBooking().getFirstname(), booking.getFirstname());
        Assert.assertEquals(bookingResponse.getBooking().getLastname(), booking.getLastname());
        Assert.assertEquals(bookingResponse.getBooking().getTotalprice(), booking.getTotalprice());
        Assert.assertEquals(bookingResponse.getBooking().getDepositpaid(), booking.getDepositpaid());
        Assert.assertEquals(bookingResponse.getBooking().getBookingdates().getCheckin(),
                booking.getBookingdates().getCheckin());
        Assert.assertEquals(bookingResponse.getBooking().getBookingdates().getCheckout(),
                booking.getBookingdates().getCheckout());
    }

    @DataProvider(name = "bookingPayloads")
    public Object[][] bookingPayloads() {
        return new Object[][]{
                {new Booking("Alice", "Anderson", 250, true,
                        new BookingDates("2026-05-01", "2026-05-10"), "Late checkout")},
                {new Booking("Bob", "Baker", 0, false,
                        new BookingDates("2026-06-01", "2026-06-02"), "")},
                {new Booking("Zoe", "Zimmerman", 9999, true,
                        new BookingDates("2027-01-01", "2027-01-02"), "Sea view")}
        };
    }

    @Test(dataProvider = "bookingPayloads",
            description = "POST /booking succeeds across a range of realistic data sets")
    public void testCreateBookingWithMultipleDataSets(Booking booking) {
        Response response = bookingClient.createBooking(booking);

        Assert.assertEquals(response.getStatusCode(), 200);
        BookingResponse bookingResponse = response.as(BookingResponse.class);
        Assert.assertEquals(bookingResponse.getBooking().getFirstname(), booking.getFirstname());
        Assert.assertEquals(bookingResponse.getBooking().getLastname(), booking.getLastname());
    }

    @Test(description = "POST /booking succeeds when the optional additionalneeds field is omitted")
    public void testCreateBookingWithoutOptionalField() {
        Booking booking = new Booking("Tom", "Taylor", 150, true,
                new BookingDates("2026-07-01", "2026-07-05"), null);

        Response response = bookingClient.createBooking(booking);
        Assert.assertEquals(response.getStatusCode(), 200);
    }
}
