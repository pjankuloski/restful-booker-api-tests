package com.qa.restfulbooker.models;

/**
 * Maps a single element of the array returned by GET /booking:
 * { "bookingid": 1 }
 */
public class BookingId {

    private int bookingid;

    public int getBookingid() {
        return bookingid;
    }

    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }
}
