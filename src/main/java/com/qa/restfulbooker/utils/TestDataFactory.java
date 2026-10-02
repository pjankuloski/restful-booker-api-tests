package com.qa.restfulbooker.utils;

import com.qa.restfulbooker.models.Booking;
import com.qa.restfulbooker.models.BookingDates;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Generates randomized-but-valid Booking payloads so tests don't collide with
 * each other's data on a shared public demo API, and don't hardcode brittle values.
 */
public final class TestDataFactory {

    private static final String[] FIRST_NAMES =
            {"John", "Jane", "Michael", "Emma", "Chris", "Laura", "Robert", "Anna"};
    private static final String[] LAST_NAMES =
            {"Smith", "Brown", "Johnson", "Williams", "Davis", "Miller", "Wilson"};

    private TestDataFactory() {
    }

    public static Booking randomBooking() {
        LocalDate checkin = LocalDate.now().plusDays(ThreadLocalRandom.current().nextInt(1, 30));
        LocalDate checkout = checkin.plusDays(ThreadLocalRandom.current().nextInt(1, 14));

        return new Booking(
                randomFrom(FIRST_NAMES),
                randomFrom(LAST_NAMES),
                ThreadLocalRandom.current().nextInt(50, 1000),
                ThreadLocalRandom.current().nextBoolean(),
                new BookingDates(checkin.toString(), checkout.toString()),
                "Breakfast"
        );
    }

    public static Booking randomBooking(String firstname, String lastname) {
        Booking booking = randomBooking();
        booking.setFirstname(firstname);
        booking.setLastname(lastname);
        return booking;
    }

    private static String randomFrom(String[] values) {
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }
}
