package org.example;

public class Booking {
    private int ID;
    private Room room;
    private Customer customer;
    private String checkIn;
    private String checkOut;

    public Booking(int ID, Room room, Customer customer, String checkIn, String checkOut) {
        this.ID = ID;
        this.room = room;
        this.customer = customer;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public int getStayDurationDays() {
//        TODO
        return 0;
    }
}
