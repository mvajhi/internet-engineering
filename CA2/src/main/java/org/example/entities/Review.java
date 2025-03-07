package org.example.entities;

import java.time.LocalDateTime;

public class Review {
    private String username;
    private String bookTitle;
    private int rate;
    private String comment;
    private LocalDateTime date;

    public Review(String username, String bookTitle, int rate, String comment) {
        this.username = username;
        this.bookTitle = bookTitle;
        this.rate = rate;
        this.comment = comment;
        this.date = LocalDateTime.now();
    }

    public String getUsername() { return username; }
    public String getBookTitle() { return bookTitle; }
    public int getRate() { return rate; }
    public String getComment() { return comment; }
    public LocalDateTime getDate() { return date; }
}