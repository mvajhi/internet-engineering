package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

public class Review {
    private String username;
    private String bookTitle;
    private int rate;
    private String comment;
    @JsonIgnore
    private LocalDateTime date;

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

    public String getUsername() { return username; }
    public String getBookTitle() { return bookTitle; }
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