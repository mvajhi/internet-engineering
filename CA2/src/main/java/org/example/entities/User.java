package org.example.entities;

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

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public Address getAddress() { return address; }
    public int getBalance() { return balance; }

    public void setBalance(int balance) { this.balance = balance; }

    public void reduceCredit(int credit){
        this.balance -= credit;
    }
}

