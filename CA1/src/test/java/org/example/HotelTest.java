package org.example;

import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HotelTest {
    @Test
    public void GIVEN_Json_WHEN_parseJson_THEN_returnHotel() {
        // Given
        String Json = """
                {
                  "customers": [
                    {
                      "ssn": 1,
                      "name": "John Doe",
                      "phone": "+1234567890",
                      "age": 25
                    }
                  ],
                  "rooms": [
                    {
                      "id": 1,
                      "capacity": 2
                    }
                  ],
                  "bookings": [
                    {
                      "id": 1,
                      "room_id": 1,
                      "customer_id": 1,
                      "check_in": "2021-01-01 12:00:00",
                      "check_out": "2021-01-03 12:00:00"
                    }
                  ]
                }
                """;
        Hotel hotel = new Hotel();

        // When
        try {
            hotel.parseJson(Json);
        } catch (IOException e) {
            Assert.fail();
        }

        // Then
        Assert.assertEquals(1, hotel.getCustomers().size());
        Assert.assertEquals(1, hotel.getRooms().size());
        Assert.assertEquals(1, hotel.getBookings().size());

        Customer customer = hotel.getCustomers().get(1);
        Assert.assertEquals(1, customer.NID);
        Assert.assertEquals("John Doe", customer.name);
        Assert.assertEquals("+1234567890", customer.phone);
        Assert.assertEquals(25, customer.age);

        Room room = hotel.getRooms().get(1);
        Assert.assertEquals(1, room.Number);
        Assert.assertEquals(2, room.Capacity);

        Booking booking = hotel.getBookings().get(1);
        Assert.assertEquals(1, booking.getID());
        Assert.assertEquals(2, booking.getStayDurationDays());
        Assert.assertEquals(1, booking.getRoom().Number);
        Assert.assertEquals(2, booking.getRoom().Capacity);
        Assert.assertEquals(1, booking.getCustomer().NID);
        Assert.assertEquals("John Doe", booking.getCustomer().name);
        Assert.assertEquals(LocalDateTime.of(2021, 1, 1, 12, 0), booking.getCheckIn());
        Assert.assertEquals(LocalDateTime.of(2021, 1, 3, 12, 0), booking.getCheckOut());
    }

    @Test
    public void GIVEN_Hotel_WHEN_logState_THEN_returnJson() {
        // Given
        Hotel hotel = new Hotel();
        Customer customer = new Customer(1, "John Doe", "+1234567890", 25);
        Room room = new Room(1, 2);
        LocalDateTime checkIn = LocalDateTime.of(2021, 1, 1, 12, 0);
        LocalDateTime checkOut = LocalDateTime.of(2021, 1, 3, 12, 0);
        Booking booking = new Booking(1, room, customer, checkIn, checkOut);
        hotel.getCustomers().put(1, customer);
        hotel.getRooms().put(1, room);
        hotel.getBookings().put(1, booking);

        // When
        String Json = hotel.logState();

        // Then
        String expectedJson = """
                {
                  "customers": [
                    {
                      "ssn": 1,
                      "name": "John Doe",
                      "phone": "+1234567890",
                      "age": 25
                    }
                  ],
                  "rooms": [
                    {
                      "id": 1,
                      "capacity": 2
                    }
                  ],
                  "bookings": [
                    {
                      "id": 1,
                      "room_id": 1,
                      "customer_id": 1,
                      "check_in": "2021-01-01 12:00:00",
                      "check_out": "2021-01-03 12:00:00"
                    }
                  ]
                }
                """;
        try {
            JSONAssert.assertEquals(expectedJson, Json, JSONCompareMode.LENIENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void GIVEN_EmptyHotel_WHEN_logState_THEN_returnEmptyJson() {
        // Given
        Hotel hotel = new Hotel();

        // When
        String Json = hotel.logState();

        // Then
        String expectedJson = """
                {
                  "customers": [],
                  "rooms": [],
                  "bookings": []
                }
                """;
        try {
            JSONAssert.assertEquals(expectedJson, Json, JSONCompareMode.LENIENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void GIVEN_BadJsonFormat_WHEN_parseJson_THEN_throwException() {
        // Given
        String Json = """
                {
                  "customers": [
                    {
                      "ssn": 1,
                      "name": "John Doe",
                      "phone": "+1234567890",
                      "age": 25
                    }
                  ],
                  "rooms": [
                    {
                      "id": 1,
                      "capacity": 2
                    }
                  ],
                  "bookings": [
                    {
                      "id": 1,
                      "room_id": 1,
                      "customer_id": 1,
                      "check_in": "2021-01-01 12:00:00"
                      "check_out": "2021-01-03 12:00:00"
                    }
                  ]
                }
                """;
        Hotel hotel = new Hotel();

        // When
        try {
            hotel.parseJson(Json);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void GIVEN_InvalidId_WHEN_parseJson_THEN_throwException() {
        // Given
        String Json = """
                {
                  "customers": [
                    {
                      "ssn": 1,
                      "name": "John Doe",
                      "phone": "+1234567890",
                      "age": 25
                    }
                  ],
                  "rooms": [
                    {
                      "id": 1,
                      "capacity": 2
                    }
                  ],
                  "bookings": [
                    {
                      "id": 1,
                      "room_id": 1,
                      "customer_id": 2,
                      "check_in": "2021-01-01 12:00:00",
                      "check_out": "2021-01-03 12:00:00"
                    }
                  ]
                }
                """;
        Hotel hotel = new Hotel();

        // When
        try {
            hotel.parseJson(Json);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void GIVEN_DuplicateId_WHEN_parseJson_THEN_throwException() {
        // Given
        String Json = """
                {
                  "customers": [
                    {
                      "ssn": 1,
                      "name": "John Doe",
                      "phone": "+1234567890",
                      "age": 25
                    },
                    {
                      "ssn": 1,
                      "name": "Jane Doe",
                      "phone": "+1234567890",
                      "age": 25
                    }
                  ],
                  "rooms": [
                    {
                      "id": 1,
                      "capacity": 2
                    }
                  ],
                  "bookings": [
                    {
                      "id": 1,
                      "room_id": 1,
                      "customer_id": 1,
                      "check_in": "2021-01-01 12:00:00",
                      "check_out": "2021-01-03 12:00:00"
                    }
                  ]
                }
                """;
        Hotel hotel = new Hotel();

        // When
        try {
            hotel.parseJson(Json);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void GIVEN_Customers_WHEN_getOldestCustomerName_THEN_returnOldestCustomerName() {
        // Given
        Hotel hotel = new Hotel();
        hotel.getCustomers().put(1, new Customer(1, "John Doe", "+1234567890", 25));
        hotel.getCustomers().put(2, new Customer(2, "Jane Doe", "+0987654321", 30));
        hotel.getCustomers().put(3, new Customer(3, "Alice", "+1122334455", 20));

        // When
        String oldestCustomerName = hotel.getOldestCustomerName();

        // Then
        Assert.assertEquals("Jane Doe", oldestCustomerName);
    }

    @Test
    public void GIVEN_NoCustomers_WHEN_getOldestCustomerName_THEN_returnNull() {
        // Given
        Hotel hotel = new Hotel();

        // When
        String oldestCustomerName = hotel.getOldestCustomerName();

        // Then
        Assert.assertNull(oldestCustomerName);
    }
}