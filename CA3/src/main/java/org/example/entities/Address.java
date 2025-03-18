package org.example.entities;

public class Address {
    public Address(){};
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

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
