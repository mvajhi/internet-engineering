package org.example.services;

import org.example.entities.*;
import org.example.request.AddUserRequest;


import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class UserService {

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
        cart.getUser().reduceCredit(totalPrice);
        receipt.addBook(cart.getPurchasedBooks());
        receipt.addBorrowedBooks(cart.getBorrowedBooks());
        receipt.setUser(cart.getUser());
        receipt.setSuccess(true);
        receipt.setDate(LocalDateTime.now());
        receipt.setMessage("Purchase completed successfully");
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
