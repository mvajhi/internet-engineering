package org.example.entities;

import org.example.response.Response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookShop {
    private List<User> users = new ArrayList<>();
    private List<Author> authors = new ArrayList<>();
    private List<Book> books = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();
    private List<Cart> baskets = new ArrayList<>();
    private List<PurchaseReceipt> receipts = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Cart> getBaskets() {
        return baskets;
    }

    public void setBaskets(List<Cart> baskets) {
        this.baskets = baskets;
    }

    public List<PurchaseReceipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<PurchaseReceipt> receipts) {
        this.receipts = receipts;
    }

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
        removeReview(review.getBookTitle(), review.getUsername());
        this.reviews.add(review);
    }

    private void removeReview(String bookTitle, String username) {
        for (Review review : reviews) {
            if (review.getBookTitle().equals(bookTitle) && review.getUsername().equals(username)) {
                reviews.remove(review);
                return;
            }
        }
    }

    public void removeCart(Cart cart){
        this.baskets.remove(cart);
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
        if (users.isEmpty()) {
            return false;
        }
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user.getRole() == Role.ADMIN;
            }
        }
        return false;
    }

    public Response checkUser(String username, Role role)
    {
        if (username == null)
            return new Response(false, "No users logged in.", null);

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (role == null || user.getRole().equals(role))
                    return new Response(true, "", null);
                return new Response(false, "user is " + (role.equals(Role.ADMIN) ? "not " : "") + "admin", null);
            }
        }

        return new Response(false, "invalid username", null);
    }

    public boolean isUserCustomer(String username) {
        if (users.isEmpty()) {
            return false;
        }
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user.getRole() == Role.CUSTOMER;
            }
        }
        return false;
    }

    public boolean isBookNameUnique(String key) {
        for (Book book : books) {
            if (book.getTitle().equals(key)) {
                return false;
            }
        }
        return true;
    }

    public Book findBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null;
    }

    public Author findAuthor(String name) {
        for (Author author : authors) {
            if (author.getName().equals(name)) {
                return author;
            }
        }
        return null;
    }

    public User findUser(String name) {
        for (User user : users) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<Review> findReviews(String title) {
        return reviews.stream().filter(e -> e.getBookTitle().equals(title)).toList();
    }
}
