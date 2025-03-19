package org.example.entities;

import java.util.Objects;

public class User {
    private String username;
    private String password;
    private String email;
    private Role role;
    private Address address;
    private int balance;

    public User(String username, String password, String email, Role role, Address address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.address = address;
        this.balance = 0;
    }

    public User() {
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public Address getAddress() { return address; }
    public int getBalance() { return balance; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void reduceCredit(int credit){
        this.balance -= credit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return balance == user.balance && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && role == user.role && Objects.equals(address, user.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email, role, address, balance);
    }

    public boolean isAdmin(){
        return Objects.equals(role, Role.ADMIN);
    }

    public User copy() {
        User userCopy = new User();
        userCopy.username = username;
        userCopy.role = role;
        userCopy.email = email;
        return userCopy;
    }
}

