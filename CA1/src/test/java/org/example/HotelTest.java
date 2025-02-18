package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
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
            Assertions.fail();
        }

        // Then
        Assertions.assertEquals(1, hotel.getCustomers().size());
        Assertions.assertEquals(1, hotel.getRooms().size());
        Assertions.assertEquals(1, hotel.getBookings().size());

        Customer customer = hotel.getCustomers().get(1);
        Assertions.assertEquals(1, customer.NID);
        Assertions.assertEquals("John Doe", customer.name);
        Assertions.assertEquals("+1234567890", customer.phone);
        Assertions.assertEquals(25, customer.age);

        Room room = hotel.getRooms().get(1);
        Assertions.assertEquals(1, room.Number);
        Assertions.assertEquals(2, room.Capacity);

        Booking booking = hotel.getBookings().get(1);
        Assertions.assertEquals(1, booking.getID());
        Assertions.assertEquals(1, booking.getRoom().Number);
        Assertions.assertEquals(2, booking.getRoom().Capacity);
        Assertions.assertEquals(1, booking.getCustomer().NID);
        Assertions.assertEquals("John Doe", booking.getCustomer().name);
        Assertions.assertEquals(LocalDateTime.of(2021, 1, 1, 12, 0), booking.getCheckIn());
        Assertions.assertEquals(LocalDateTime.of(2021, 1, 3, 12, 0), booking.getCheckOut());
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
            Assertions.assertEquals(expectedJson, Json, String.valueOf(JSONCompareMode.LENIENT));
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
            Assertions.assertEquals(expectedJson, Json, String.valueOf(JSONCompareMode.LENIENT));
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
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof IOException);
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
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof IllegalArgumentException);
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
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof IllegalArgumentException);
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
        Assertions.assertEquals("Jane Doe", oldestCustomerName);
    }

    @Test
    public void GIVEN_NoCustomers_WHEN_getOldestCustomerName_THEN_returnNull() {
        // Given
        Hotel hotel = new Hotel();

        // When
        String oldestCustomerName = hotel.getOldestCustomerName();

        // Then
        Assertions.assertNull(oldestCustomerName);
    }

    @Test
    public void GIVEN_Bookings_WHEN_getCustomerPhonesByRoomNumber_THEN_returnCustomerPhones() {
        // Given
        Hotel hotel = new Hotel();
        Room room1 = new Room(1, 2);
        Room room2 = new Room(2, 3);
        hotel.getRooms().put(1, room1);
        hotel.getRooms().put(2, room2);

        Customer customer1 = new Customer(1, "John Doe", "+1234567890", 25);
        Customer customer2 = new Customer(2, "Jane Doe", "+0987654321", 30);
        hotel.getCustomers().put(1, customer1);
        hotel.getCustomers().put(2, customer2);

        LocalDateTime checkIn = LocalDateTime.of(2021, 1, 1, 12, 0);
        LocalDateTime checkOut = LocalDateTime.of(2021, 1, 3, 12, 0);
        hotel.getBookings().put(1, new Booking(1, room1, customer1, checkIn, checkOut));
        hotel.getBookings().put(2, new Booking(2, room1, customer2, checkIn, checkOut));

        // When
        ArrayList<String> phones = hotel.getCustomerPhonesByRoomNumber(1);

        // Then
        Assertions.assertEquals(2, phones.size());
        Assertions.assertTrue(phones.contains("+1234567890"));
        Assertions.assertTrue(phones.contains("+0987654321"));
    }

    @Test
    public void GIVEN_NoBookingsForRoom_WHEN_getCustomerPhonesByRoomNumber_THEN_returnEmptyList() {
        // Given
        Hotel hotel = new Hotel();
        Room room1 = new Room(1, 2);
        hotel.getRooms().put(1, room1);

        // When
        ArrayList<String> phones = hotel.getCustomerPhonesByRoomNumber(1);

        // Then
        Assertions.assertTrue(phones.isEmpty());
    }

    @Test
    public void GIVEN_Rooms_WHEN_getRoomsWithMinCapacity_THEN_returnFilteredRooms() {
        // Given
        Hotel hotel = new Hotel();
        hotel.getRooms().put(1, new Room(1, 2));
        hotel.getRooms().put(2, new Room(2, 4));
        hotel.getRooms().put(3, new Room(3, 3));

        // When
        ArrayList<Room> filteredRooms = hotel.getRooms(3);

        // Then
        Assertions.assertEquals(2, filteredRooms.size());
        Assertions.assertTrue(filteredRooms.stream().anyMatch(room -> room.Number == 2));
        Assertions.assertTrue(filteredRooms.stream().anyMatch(room -> room.Number == 3));
    }

    @Test
    public void GIVEN_NoRoomsWithMinCapacity_WHEN_getRoomsWithMinCapacity_THEN_returnEmptyList() {
        // Given
        Hotel hotel = new Hotel();
        hotel.getRooms().put(1, new Room(1, 2));
        hotel.getRooms().put(2, new Room(2, 3));

        // When
        ArrayList<Room> filteredRooms = hotel.getRooms(4);

        // Then
        Assertions.assertTrue(filteredRooms.isEmpty());
    }
}