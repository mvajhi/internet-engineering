package org.example.entities;

public class Address {
    public Address(String country, String city) {
        this.country = country;
        this.city = city;
    }

    String country;
    String city;

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }
}
