package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
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
        Assertions.assertEquals(1, booking.getID());
        Assertions.assertEquals(room, booking.getRoom());
        Assertions.assertEquals(customer, booking.getCustomer());
        Assertions.assertEquals(checkIn, booking.getCheckIn());
        Assertions.assertEquals(checkOut, booking.getCheckOut());
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
            Assertions.fail();
        } catch (Exception e) {
            // Then
            Assertions.assertTrue(e instanceof IllegalArgumentException);
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
        Assertions.assertEquals(Duration.ofDays(2), stayDurationDays);
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
        Assertions.assertEquals(Duration.ofHours((2*24 + 23) - (12)), stayDurationDays);
    }
}
