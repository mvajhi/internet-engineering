package org.example;

import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;

public class BookingTest {
    @Test
    public void GIVEN_booking_WHEN_BookingConstructor_THEN_returnBooking() {
        // Given
        Room room = new Room(1, 2);
        Customer customer = new Customer(1, "John Doe", "+1234567890", 25);
        LocalDateTime checkIn = LocalDateTime.of(2021, 1, 1, 12, 0);
        LocalDateTime checkOut = LocalDateTime.of(2021, 1, 3, 12, 0);

        // When
        Booking booking = new Booking(1, room, customer, checkIn, checkOut);

        // Then
        Assert.assertEquals(1, booking.getID());
        Assert.assertEquals(room, booking.getRoom());
        Assert.assertEquals(customer, booking.getCustomer());
        Assert.assertEquals(checkIn, booking.getCheckIn());
        Assert.assertEquals(checkOut, booking.getCheckOut());
    }

    @Test
    public void GIVEN_InvalidCheckTime_WHEN_BookingConstructor_THEN_throwIllegalArgumentException() {
        // Given
        Room room = new Room(1, 2);
        Customer customer = new Customer(1, "John Doe", "+1234567890", 25);
        LocalDateTime checkIn = LocalDateTime.of(2021, 1, 3, 12, 0);
        LocalDateTime checkOut = LocalDateTime.of(2021, 1, 1, 12, 0);

        // When
        try {
            Booking booking = new Booking(1, room, customer, checkIn, checkOut);
            Assert.fail();
        } catch (Exception e) {
            // Then
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void GIVEN_booking_WHEN_getStayDurationDays_THEN_returnStayDurationDays() {
        // Given
        Room room = new Room(1, 2);
        Customer customer = new Customer(1, "John Doe", "+1234567890", 25);
        LocalDateTime checkIn = LocalDateTime.of(2021, 1, 1, 12, 0);
        LocalDateTime checkOut = LocalDateTime.of(2021, 1, 3, 12, 0);
        Booking booking = new Booking(1, room, customer, checkIn, checkOut);

        // When
        Duration stayDurationDays = booking.getStayDurationDays();

        // Then
        Assert.assertEquals(Duration.ofDays(2), stayDurationDays);
    }

    @Test
    public void GIVEN_differentHourInCheckOut_WHEN_getStayDurationDays_THEN_returnStayDurationDays() {
        // Given
        Room room = new Room(1, 2);
        Customer customer = new Customer(1, "John Doe", "+1234567890", 25);
        LocalDateTime checkIn = LocalDateTime.of(2021, 1, 1, 12, 0);
        LocalDateTime checkOut = LocalDateTime.of(2021, 1, 3, 23, 0);
        Booking booking = new Booking(1, room, customer, checkIn, checkOut);

        // When
        Duration stayDurationDays = booking.getStayDurationDays();

        // Then
        Assert.assertEquals(Duration.ofHours((2*24 + 23) - (12)), stayDurationDays);
    }
}
