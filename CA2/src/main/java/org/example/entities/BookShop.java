package org.example.entities;

import java.util.ArrayList;
import java.util.List;

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
    public Cart getBasketByUsername(String username){
        List<Cart> list = this.baskets.stream().filter(e ->e.getUser().getUsername().equals(username)).toList();
        if(list.isEmpty())
            return null;
        else
            return list.get(0);
    }
    public List<PurchaseReceipt> getReceiptsByUsername(String username){
        return this.receipts.stream().filter(r -> r.getUser().getUsername().equals(username)).toList();
    }
}
