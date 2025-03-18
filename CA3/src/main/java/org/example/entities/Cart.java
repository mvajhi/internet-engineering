package org.example.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private User user;
    private List<Book> purchasedBooks = new ArrayList<>();
    private Map<Book, Integer> borrowedBooks = new HashMap<>();
    public void addPurchasedBook(Book book){
        this.purchasedBooks.add(book);
    }
    public void addBorrowedBook(Book book, int days){ this.borrowedBooks.put(book, days);}

    public User getUser() {
        return user;
    }

    public List<Book> getPurchasedBooks() {
        return purchasedBooks;
    }

    public Map<Book, Integer> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void removePurchasedBook(Book book){
        this.purchasedBooks.remove(book);
    }

    public boolean hasPurchasedBook(Book book){
        return this.purchasedBooks.contains(book);
    }

    public void setPurchasedBooks(List<Book> purchasedBooks) {
        this.purchasedBooks = purchasedBooks;
    }

    public void setBorrowedBooks(Map<Book, Integer> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
}
