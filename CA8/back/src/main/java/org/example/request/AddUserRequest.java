package org.example.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.example.entities.Address;

public class AddUserRequest {
    String role;
    String username;
    String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
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
