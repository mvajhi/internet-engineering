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
        Response response = new Response(true, "User added successfully.", null);
        User newUser = createUser(request);
        if (newUser == null) {
            return new Response(false, "invalid user parameter", null);
        }
        if (!bookShop.isEmailUnique(newUser.getEmail())) {
            response.setSuccess(false);
            response.setMessage("Email is not unique");
        }
        if (!bookShop.isUsernameUnique(newUser.getUsername())) {
            response.setSuccess(false);
            response.setMessage("username is not unique");
        }
        this.bookShop.addUser(newUser);
        return response;
    }

    public User createUser(AddUserRequest request) {
        User newUser = new User(request.getUsername(), request.getPassword(), request.getEmail(),
                getRoleByString(request.getRole()),
                getAddressByString(request.getAddress().getCountry(), request.getAddress().getCity()));
        boolean isUserValid = isUserValid(newUser);
        if (!isUserValid)
            return null;
        return newUser;
    }

    public PurchaseReceipt finishPurchase(Cart cart) {
        PurchaseReceipt receipt = new PurchaseReceipt();
        if (cart.getPurchasedBooks().isEmpty() && cart.getBorrowedBooks().isEmpty()) {
            receipt.setSuccess(false);
            receipt.setMessage("Empty Book List");
            return receipt;
        }
        int totalPrice =getTotalPrice(cart);
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

    private boolean isUserValid(User newUser) {
        boolean isValid = true;
        Pattern usernamePattern = Pattern.compile("^[0-9a-z_-]+$", Pattern.CASE_INSENSITIVE);
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$", Pattern.CASE_INSENSITIVE);
        if (!usernamePattern.matcher(newUser.getUsername()).matches()) {
            isValid = false;
        }
        if (newUser.getPassword().length() < 4) {
            isValid = false;
        }
        if (!emailPattern.matcher(newUser.getEmail()).matches()) {
            isValid = false;
        }
        if (newUser.getRole() == null) {
            isValid = false;
        }
        return isValid;
    }

    public User findUser(String username) {
        return bookShop.findUser(username);
    }
}
