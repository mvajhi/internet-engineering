package org.example.services;

import org.example.entities.*;
import org.example.request.AddUserRequest;
import org.example.response.BookResponses;
import org.example.response.CartResponses;
import org.example.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    BookShop bookShop;

    BookService bookService;

    public void setBookService(BookService bookService) {
        this.bookService=bookService;
    }

    public UserService(BookShop bookShop) {
        this.bookShop = bookShop;
    }

    public Response addUser(AddUserRequest request) {
        User newUser = createUser(request);
        Response validate = validateUser(newUser);
        if (!validate.isSuccess())
            return validate;
        if (!bookShop.isEmailUnique(newUser.getEmail()))
            return new Response(false, "email is not unique", null);
        if (!bookShop.isUsernameUnique(newUser.getUsername()))
            return new Response(false, "username is not unique", null);
        this.bookShop.addUser(newUser);
        return new Response(true, "User added successfully.", null);
    }

    public User createUser(AddUserRequest request) {
        String username = request.getUsername() != null ? request.getUsername() : "";
        String password = request.getPassword() != null ? request.getPassword() : "";
        String email = request.getEmail() != null ? request.getEmail() : "";
        Role role = request.getRole() != null ? getRoleByString(request.getRole()) : null;
        String country = request.getAddress() != null && request.getAddress().getCountry() != null ? request.getAddress().getCountry() : "";
        String city = request.getAddress() != null && request.getAddress().getCity() != null ? request.getAddress().getCity() : "";

        return new User(username, password, email, role, getAddressByString(country, city));
    }

    public PurchaseReceipt finishPurchase(Cart cart) {
        PurchaseReceipt receipt = new PurchaseReceipt();
        if (cart.getPurchasedBooks().isEmpty() && cart.getBorrowedBooks().isEmpty()) {
            receipt.setSuccess(false);
            receipt.setMessage("Empty Book List");
            return receipt;
        }
        int totalPrice = getTotalPrice(cart);
        if (cart.getUser().getBalance() < totalPrice) {
            receipt.setSuccess(false);
            receipt.setMessage("User Credit Is Not Enough");
            return receipt;
        }
        receipt.setBooks(cart.getPurchasedBooks());
        receipt.setBorrowedBooks(cart.getBorrowedBooks());
        receipt.setUser(cart.getUser());
        receipt.setSuccess(true);
        receipt.setDate(LocalDateTime.now());
        receipt.setMessage("Purchase completed successfully");
        receipt.setTotalPrice(totalPrice);
        cart.getUser().reduceCredit(totalPrice);
        return receipt;

    }

    private static int getTotalPrice(Cart cart) {
        return cart.getPurchasedBooks().stream().map(Book::getPrice).reduce(0, Integer::sum)
               + cart.getBorrowedBooks().entrySet().stream().mapToInt(entry -> entry.getKey().getPrice() * entry.getValue() / 10).sum();
    }

    public CartResponses showShoppingCart(Cart cart) {
        CartResponses cartResponses = new CartResponses();
        cartResponses.setUsername(cart.getUser().getUsername());
        cartResponses.setTotalCost(getTotalPrice(cart));
        cartResponses.setItems(getBookResponsesList(cart.getPurchasedBooks(), cart.getBorrowedBooks()));
        return cartResponses;
    }

    public List<BookResponses> getBookResponsesList(List<Book> purchasedBooks, Map<Book, Integer> borrowedBooks) {
        List<BookResponses> bookResponses=getBookResponsesList(purchasedBooks);
        bookResponses.addAll(getBookResponsesList(borrowedBooks));
        return bookResponses;
    }

    public List<BookResponses> getBookResponsesList(Map<Book, Integer> borrowedBooks) {
        List<BookResponses> bookResponses = new ArrayList<>();
        for (Map.Entry<Book, Integer> entry : borrowedBooks.entrySet()) {
            BookResponses bookResponse=getBookResponse(entry);
            bookResponses.add(bookResponse);
        }
        return bookResponses;
    }

    public BookResponses getBookResponse(Map.Entry<Book, Integer> entry, LocalDateTime date) {
        Book book = entry.getKey();
        Integer borrowedDays = entry.getValue();
        BookResponses bookResponse = bookService.createTmpBookResponse(book);
        bookResponse.setBorrowed(true);
        bookResponse.setFinalPrice(bookResponse.getPrice() * borrowedDays / 10);
        bookResponse.setBorrowedDays(borrowedDays);
        bookResponse.setBorrowedDate(date.plusDays((long) borrowedDays));
        return bookResponse;
    }

    public BookResponses getBookResponse(Map.Entry<Book, Integer> entry) {
        Book book = entry.getKey();
        Integer borrowedDays = entry.getValue();
        BookResponses bookResponse = bookService.createTmpBookResponse(book);
        bookResponse.setBorrowed(true);
        bookResponse.setFinalPrice(bookResponse.getPrice() * borrowedDays / 10);
        bookResponse.setBorrowedDays(borrowedDays);
        return bookResponse;
    }

    public List<BookResponses> getBookResponsesList(List<Book> purchasedBooks) {
        List<BookResponses> bookResponses = new ArrayList<>();
        for (Book book : purchasedBooks) {
            BookResponses bookResponse=getBookResponse(book);
            bookResponses.add(bookResponse);
        }
        return bookResponses;
    }

    public BookResponses getBookResponse(Book book) {
        BookResponses bookResponse = bookService.createTmpBookResponse(book);
        bookResponse.setBorrowed(false);
        bookResponse.setFinalPrice(bookResponse.getPrice());
        return bookResponse;
    }

    public void addCredit(User user, int credit) {
        user.setBalance(user.getBalance() + credit);
    }

    private Role getRoleByString(String role) {
        if (role.equals("customer")) {
            return Role.CUSTOMER;
        } else if (role.equals("admin")) {
            return Role.ADMIN;
        } else return null;
    }

    private Address getAddressByString(String country, String city) {
        return new Address(country, city);
    }

    private Response validateUser(User newUser) {
        boolean isValid = false;
        String error = "";
        Pattern usernamePattern = Pattern.compile("^[0-9a-z_-]+$", Pattern.CASE_INSENSITIVE);
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", Pattern.CASE_INSENSITIVE);
        if (!usernamePattern.matcher(newUser.getUsername()).matches())
            error = "invalid username";
        else if (newUser.getPassword().length() < 4)
            error = "password is too short, less than 4";
        else if (!emailPattern.matcher(newUser.getEmail()).matches())
            error = "invalid email";
        else if (newUser.getRole() == null)
            error = "invalid role";
        else if (newUser.getAddress().getCity().isEmpty() || newUser.getAddress().getCountry().isEmpty())
            error = "invalid address";
        else
            isValid = true;
        return new Response(isValid, error, null);
    }

    public User findUser(String username) {
        return bookShop.findUser(username);
    }
}
