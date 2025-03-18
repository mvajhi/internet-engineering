package org.example.services;

import org.example.config.BookShopConfig;
import org.example.entities.*;
import org.example.request.AddUserRequest;
import org.example.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    BookShop bookShop;

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
        if (cart.getPurchasedBooks().isEmpty()) {
            receipt.setSuccess(false);
            receipt.setMessage("Empty Book List");
            return receipt;
        }
        int totalPrice = cart.getPurchasedBooks().stream().map(Book::getPrice).reduce(0, Integer::sum)
                + cart.getBorrowedBooks().entrySet().stream().mapToInt(entry -> entry.getKey().getPrice() * entry.getValue() / 10).sum();
        if (cart.getUser().getBalance() < totalPrice) {
            receipt.setSuccess(false);
            receipt.setMessage("User Credit Is Not Enough");
            return receipt;
        }
        receipt.addBook(cart.getPurchasedBooks());
        receipt.addBorrowedBooks(cart.getBorrowedBooks());
        receipt.setUser(cart.getUser());
        receipt.setSuccess(true);
        receipt.setDate(LocalDateTime.now());
        receipt.setMessage("Purchase completed successfully");
        cart.getUser().reduceCredit(totalPrice);
        return receipt;

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
}
