package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Room {
    @JsonProperty("id")
    public int Number;
    @JsonProperty("capacity")
    public int Capacity;

    public Room() {
    }

    public Room(int Number, int Capacity) {
        this.Number = Number;
        this.Capacity = Capacity;
    }
}
