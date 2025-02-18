package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
        return new ArrayList<>(rooms.values());
    }

    public ArrayList<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    public ArrayList<Booking> getAllBookings() {
        return new ArrayList<>(bookings.values());
    }

    public ArrayList<Room> getRooms(int minCapacity) {
        return rooms.values().stream()
                .filter(room -> room.Capacity >= minCapacity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public String getOldestCustomerName() {
        return customers.values().stream()
                .min((c1, c2) -> Integer.compare(c1.age, c2.age))
                .map(customer -> customer.name)
                .orElse(null);
    }

    public ArrayList<String> getCustomerPhonesByRoomNumber(int roomNumber) {
        return bookings.values().stream()
                .filter(booking -> booking.getRoom().Number == roomNumber)
                .map(booking -> booking.getCustomer().phone)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public String logState() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> state = new HashMap<>();
            state.put("customers", customers.values());
            state.put("rooms", rooms.values());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            state.put("bookings", bookings.values().stream().map(booking -> {
                Map<String, Object> bookingMap = new HashMap<>();
                bookingMap.put("id", booking.getID());
                bookingMap.put("room_id", booking.getRoom().Number);
                bookingMap.put("customer_id", booking.getCustomer().NID);
                bookingMap.put("check_in", booking.getCheckIn().format(formatter));
                bookingMap.put("check_out", booking.getCheckOut().format(formatter));
                return bookingMap;
            }).collect(Collectors.toList()));
            return objectMapper.writeValueAsString(state);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void parseJson(String json) throws IOException, IllegalArgumentException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);

        JsonNode customersNode = rootNode.get("customers");
        parseCustomer(customersNode, objectMapper);

        JsonNode roomsNode = rootNode.get("rooms");
        parseRoom(roomsNode, objectMapper);

        JsonNode bookingsNode = rootNode.get("bookings");
        parseBooking(bookingsNode);
    }

    private void parseBooking(JsonNode bookingsNode) throws IllegalArgumentException {
        if (bookingsNode != null) {
            for (JsonNode bookingNode : bookingsNode) {
                int roomId = bookingNode.get("room_id").asInt();
                int customerId = bookingNode.get("customer_id").asInt();
                Room room = rooms.get(roomId);
                Customer customer = customers.get(customerId);
                if (customer == null || room == null) {
                    throw new IllegalArgumentException("Invalid id in booking data");
                }
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
    }

    private void parseRoom(JsonNode roomsNode, ObjectMapper objectMapper) throws JsonProcessingException {
        if (roomsNode != null) {
            for (JsonNode roomNode : roomsNode) {
                Room room = objectMapper.treeToValue(roomNode, Room.class);
                if (rooms.containsKey(room.Number)) {
                    throw new IllegalArgumentException("Duplicate room id");
                }
                rooms.put(room.Number, room);
            }
        }
    }

    private void parseCustomer(JsonNode customersNode, ObjectMapper objectMapper) throws JsonProcessingException {
        if (customersNode != null) {
            for (JsonNode customerNode : customersNode) {
                Customer customer = objectMapper.treeToValue(customerNode, Customer.class);
                if (customers.containsKey(customer.NID)) {
                    throw new IllegalArgumentException("Duplicate customer id");
                }
                customers.put(customer.NID, customer);
            }
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
