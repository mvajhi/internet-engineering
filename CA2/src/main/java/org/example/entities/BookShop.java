package org.example.entities;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class BookShop {
    private List<User> users = new ArrayList<>();
    private List<Author> authors = new ArrayList<>();
    private List<Book> books = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();
    private List<Cart> baskets = new ArrayList<>();
    private List<PurchaseReceipt> receipts = new ArrayList<>();
    public void addUser(User user){
        this.users.add(user);
    }
    public void addAuthor(Author author){
        this.authors.add(author);
    }
    public void addBook(Book book){
        this.books.add(book);
    }
    public void addBasket(Cart cart){
        this.baskets.add(cart);
    }
    public void addRecipt(PurchaseReceipt receipt) {this.receipts.add(receipt);}
    public void addReview(Review review){
        this.reviews.add(review);
    }
    public Cart getCartByUsername(String username){
        List<Cart> list = this.baskets.stream().filter(e ->e.getUser().getUsername().equals(username)).toList();
        if(list.isEmpty())
            return null;
        else
            return list.get(0);
    }
    public List<PurchaseReceipt> getReceiptsByUsername(String username){
        return this.receipts.stream().filter(r -> r.getUser().getUsername().equals(username)).toList();
    }

    public boolean isEmailUnique(String email) {
        return users.stream().noneMatch(e -> e.getEmail().equals(email));
    }

    public boolean isUserExist(String username) {
        return users.stream().anyMatch(e -> e.getUsername().equals(username));

    }

    public boolean isUsernameUnique(String username) {
        return users.stream().noneMatch(e -> e.getUsername().equals(username));
    }

    public boolean isAuthorNameUnique(String name) {
        return authors.stream().noneMatch(e -> e.getName().equals(name));
    }

    public boolean isUserAdmin(String username) {
        return users.stream().filter(e -> e.getUsername().equals(username)).toList().get(0).getRole() == Role.ADMIN;
    }

    public boolean isUserCustomer(String username) {
        return users.stream().filter(e -> e.getUsername().equals(username)).toList().get(0).getRole() == Role.CUSTOMER;
    }

    public boolean isBookNameUnique(String key) {
        return books.stream().noneMatch(e -> e.getTitle().equals(key));
    }

    public Book findBook(String title) {
        List<Book> list = books.stream().filter(e -> e.getTitle().equals(title)).toList();
        if (list.isEmpty())
            return null;
        else return list.get(0);
    }

    public Author findAuther(String name) {
        List<Author> list = authors.stream().filter(e -> e.getName().equals(name)).toList();
        if (list.isEmpty())
            return null;
        else return list.get(0);
    }

    public User findUser(String name) {
        List<User> list = users.stream().filter(e -> e.getUsername().equals(name)).toList();
        if (list.isEmpty())
            return null;
        else return list.get(0);
    }

    public List<Review> getReviews() {
        return reviews;
    }
}
