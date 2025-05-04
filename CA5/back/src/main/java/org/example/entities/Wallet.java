package org.example.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Wallet")
public class Wallet {
    
    @Id
    @Column(name = "user_id")
    private Long userId;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "balance", nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;
    
    public Wallet() {
    }
    
    public Wallet(User user, BigDecimal balance) {
        this.user = user;
        this.balance = balance;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    public void addBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }
    
    public boolean deductBalance(BigDecimal amount) {
        if (this.balance.compareTo(amount) >= 0) {
            this.balance = this.balance.subtract(amount);
            return true;
        }
        return false;
    }
}