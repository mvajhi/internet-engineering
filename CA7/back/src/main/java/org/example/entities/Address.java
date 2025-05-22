package org.example.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country", nullable = false, length = 50)
    private String country;
    
    @Column(name = "city", nullable = false, length = 50)
    private String city;

    public Address() {}
    
    public Address(String country, String city) {
        this.country = country;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
