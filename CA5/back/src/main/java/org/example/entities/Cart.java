package org.example.entities;

import jakarta.persistence.*;
import org.example.response.CartResponses;
import org.example.response.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "Carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "Cart_Purchased_Books",
        joinColumns = @JoinColumn(name = "cart_id"),
        inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> purchasedBooks = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(
        name = "Cart_Borrowed_Books",
        joinColumns = @JoinColumn(name = "cart_id")
    )
    @MapKeyJoinColumn(name = "book_id")
    @Column(name = "borrow_days")
    private Map<Book, Integer> borrowedBooks = new HashMap<>();
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public void addPurchasedBook(Book book){
        this.purchasedBooks.add(book);
    }
    
    public void addBorrowedBook(Book book, int days){ 
        this.borrowedBooks.put(book, days);
    }

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
        if (this.borrowedBooks.containsKey(book)){
            this.borrowedBooks.remove(book);
        } else {
            this.purchasedBooks.remove(book);
        }
    }

    public boolean hasPurchasedBook(Book book){
        return this.purchasedBooks.contains(book) || this.borrowedBooks.containsKey(book);
    }

    public void setPurchasedBooks(List<Book> purchasedBooks) {
        this.purchasedBooks = purchasedBooks;
    }

    public void setBorrowedBooks(Map<Book, Integer> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
}
