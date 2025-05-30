package org.example.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "Purchase_Receipts")
public class PurchaseReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "Receipt_Books",
        joinColumns = @JoinColumn(name = "receipt_id"),
        inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> books = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(
        name = "Receipt_Borrowed_Books",
        joinColumns = @JoinColumn(name = "receipt_id")
    )
    @MapKeyJoinColumn(name = "book_id")
    @Column(name = "borrow_days")
    private Map<Book, Integer> borrowedBooks = new HashMap<>();
    
    @Column(name = "date", nullable = false)
    private LocalDateTime date;
    
    @Column(name = "is_success", nullable = false)
    private boolean isSuccess;
    
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;
    
    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    public PurchaseReceipt() {
        this.date = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public List<Book> getBooks() {
        return books;
    }

    public Map<Book, Integer> getBorrowedBooks() {
        return borrowedBooks;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void setBorrowedBooks(Map<Book, Integer> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
