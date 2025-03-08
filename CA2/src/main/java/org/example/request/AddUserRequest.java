package org.example.request;

import org.example.entities.Address;

public class AddUserRequest {
    String role;
    String username;
    String password;
    String email;
    Address address;

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

}
