package org.example;

import java.time.Duration;
import java.time.LocalDateTime;

public class Booking {
    private int ID;
    private Room room;
    private Customer customer;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    public Booking(int ID, Room room, Customer customer, LocalDateTime checkIn, LocalDateTime checkOut) {
        this.ID = ID;
        this.room = room;
        this.customer = customer;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public int getID() {
        return ID;
    }

    public Room getRoom() {
        return room;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    public Duration getStayDurationDays() {
        return java.time.Duration.between(checkIn, checkOut);
    }

}
