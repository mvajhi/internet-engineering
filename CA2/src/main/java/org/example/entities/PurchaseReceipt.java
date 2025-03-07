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
    public void addBook(List<Book> book){ this.books.addAll(book);}
    public void addBorrowedBooks(Map<Book,Integer> borrowedBooks){this.borrowedBooks.putAll(borrowedBooks);}

    public User getUser() {
        return user;
    }
}
