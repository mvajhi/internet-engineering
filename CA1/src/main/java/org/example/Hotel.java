package org.example;

import java.util.ArrayList;
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
