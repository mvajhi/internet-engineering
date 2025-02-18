package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer {
    @JsonProperty("ssn")
    public int NID;
    @JsonProperty("name")
    public String name;
    @JsonProperty("phone")
    public String phone;
    @JsonProperty("age")
    public int age;

    public Customer() {
    }

    public Customer(int NID, String name, String phone, int age) {
        this.NID = NID;
        this.name = name;
        this.phone = phone;
        this.age = age;
    }
}
