package org.example;

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

    public int getStayDurationDays() {
        return (int) java.time.Duration.between(checkIn, checkOut).toDaysPart();
    }

}
