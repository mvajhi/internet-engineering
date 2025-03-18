package org.example.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class PurchaseReceipt {
    private User user;
    private List<Book> books;
    private Map<Book, Integer> borrowedBooks;
    private LocalDateTime date;
    private boolean isSuccess;
    private String message;

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

    public void setSuccess(boolean b) {
        isSuccess = b;
    }
}
