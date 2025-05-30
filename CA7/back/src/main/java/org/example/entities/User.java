package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;
    
    @Column(name = "password", nullable = false, length = 100)
    private String password;
    
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", nullable = true)
    private Address address;
    
    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Wallet wallet;

    @Transient
    private Integer balance;

    public User(String username, String password, String email, Role role, Address address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.address = address;
        if (role == Role.ADMIN)
            this.balance = null;
        else
            this.balance = 0;
    }

    public User() {
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public Address getAddress() { return address; }
    
    public Integer getBalance() {
        if (role == Role.ADMIN) {
            return null;
        }
        
        if (wallet != null) {
            return wallet.getBalance().intValue();
        }
        
        return balance != null ? balance : 0;
    }

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
        if (wallet != null) {
            wallet.setBalance(java.math.BigDecimal.valueOf(balance));
        }
        getBalance();
    }

    public void addCredit(int credit) {
        if (wallet != null) {
            wallet.addBalance(java.math.BigDecimal.valueOf(credit));
        }
        getBalance();
    }

    public void reduceCredit(int credit) {
        if (wallet != null) {
            wallet.deductBalance(java.math.BigDecimal.valueOf(credit));
        } else if (balance != null) {
            this.balance -= credit;
        }
    }
    
    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && 
               Objects.equals(password, user.password) && 
               Objects.equals(email, user.email) && 
               role == user.role && 
               Objects.equals(address, user.address);
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

