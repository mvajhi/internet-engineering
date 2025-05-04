package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    @JsonIgnore
    private Book book;
    
    @Column(name = "rate", nullable = false)
    private int rate;
    
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;
    @JsonIgnore
    @Column(name = "created_at", nullable = false)
    private LocalDateTime date;
    
    @Transient
    private String username;
    
    @Transient
    private String bookTitle;

    public Review() {
        this.date = LocalDateTime.now();
    }

    public Review(String username, String bookTitle, int rate, String comment) {
        this.username = username;
        this.bookTitle = bookTitle;
        this.rate = rate;
        this.comment = comment;
        this.date = LocalDateTime.now();
    }

    public Review(String username, String bookTitle, int rate, String comment, LocalDateTime date) {
        this.username = username;
        this.bookTitle = bookTitle;
        this.rate = rate;
        this.comment = comment;
        this.date = date;
    }
    
    public Review(User user, Book book, int rate, String comment) {
        this.user = user;
        this.book = book;
        this.rate = rate;
        this.comment = comment;
        this.date = LocalDateTime.now();
        this.username = user != null ? user.getUsername() : null;
        this.bookTitle = book != null ? book.getTitle() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.username = user.getUsername();
        }
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
        if (book != null) {
            this.bookTitle = book.getTitle();
        }
    }

    public String getUsername() { 
        return username != null ? username : (user != null ? user.getUsername() : null);
    }
    
    public String getBookTitle() {
        return bookTitle != null ? bookTitle : (book != null ? book.getTitle() : null);
    }
    
    public int getRate() { return rate; }
    
    public String getComment() { return comment; }
    
    public LocalDateTime getDate() { return date; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}