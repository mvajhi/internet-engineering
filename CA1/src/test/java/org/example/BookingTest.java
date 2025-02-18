package org.example;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

public class BookingTest {
    @Test
    public void GIVEN_booking_WHEN_getStayDurationDays_THEN_returnStayDurationDays() {
        // Given
        Room room = new Room(1, 2);
        Customer customer = new Customer(1, "John Doe", "+1234567890", 25);
        LocalDateTime checkIn = LocalDateTime.of(2021, 1, 1, 12, 0);
        LocalDateTime checkOut = LocalDateTime.of(2021, 1, 3, 12, 0);
        Booking booking = new Booking(1, room, customer, checkIn, checkOut);

        // When
        int stayDurationDays = booking.getStayDurationDays();

        // Then
        Assert.assertEquals(2, stayDurationDays);
    }
}
