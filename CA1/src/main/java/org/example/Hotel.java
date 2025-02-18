package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Hotel {
    private Map<Integer, Room> rooms;
    private Map<Integer, Customer> customers;
    private Map<Integer, Booking> bookings;

    public Hotel(Map<Integer, Room> rooms, Map<Integer, Customer> customers, Map<Integer, Booking> bookings) {
        this.rooms = rooms;
        this.customers = customers;
        this.bookings = bookings;
    }

    public Hotel() {
        rooms = new HashMap<>();
        customers = new HashMap<>();
        bookings = new HashMap<>();
    }

    public ArrayList<Room> getAllRooms() {
//        TODO
        return null;
    }

    public ArrayList<Customer> getAllCustomers() {
//        TODO
        return null;
    }

    public ArrayList<Booking> getAllBookings() {
//        TODO
        return null;
    }

    public ArrayList<Room> getRooms(int minCapacity) {
//        TODO
        return null;
    }

    public String getOldestCustomerName() {
//        TODO
        return null;
    }

    public ArrayList<String> getCustomerPhonesByRoomNumber(int roomNumber) {
//        TODO
        return null;
    }

    public String logState() {
//        TODO
        return null;
    }
    
    public void parseJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode rootNode = objectMapper.readTree(json);

                JsonNode customersNode = rootNode.get("customers");
                if (customersNode != null) {
                    for (JsonNode customerNode : customersNode) {
                        Customer customer = objectMapper.treeToValue(customerNode, Customer.class);
                        customers.put(customer.NID, customer);
                    }
                }

                JsonNode roomsNode = rootNode.get("rooms");
                if (roomsNode != null) {
                    for (JsonNode roomNode : roomsNode) {
                        Room room = objectMapper.treeToValue(roomNode, Room.class);
                        rooms.put(room.Number, room);
                    }
                }

                JsonNode bookingsNode = rootNode.get("bookings");
                if (bookingsNode != null) {
                    for (JsonNode bookingNode : bookingsNode) {
                        int roomId = bookingNode.get("room_id").asInt();
                        int customerId = bookingNode.get("customer_id").asInt();
                        Room room = rooms.get(roomId);
                        Customer customer = customers.get(customerId);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        Booking booking = new Booking(
                                bookingNode.get("id").asInt(),
                                room,
                                customer,
                                LocalDateTime.parse(bookingNode.get("check_in").asText(), formatter),
                                LocalDateTime.parse(bookingNode.get("check_out").asText(), formatter)
                        );
                        bookings.put(booking.getID(), booking);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public Map<Integer, Room> getRooms() {
        return rooms;
    }

    public Map<Integer, Customer> getCustomers() {
        return customers;
    }

    public Map<Integer, Booking> getBookings() {
        return bookings;
    }
}
