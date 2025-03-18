package org.example.controller;

import org.example.request.*;
import org.example.services.BookShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookShopController {

    @Autowired
    BookShopService bookShopService;

    @RequestMapping("/user")
    @PostMapping
    public String addUser(@RequestBody AddUserRequest request) {
        return bookShopService.addUser(request).convertToString();
    }

    @RequestMapping("/author")
    @PostMapping
    public String addAuthor(@RequestBody AddAuthorRequest request) {
        return bookShopService.addAuthor(request).convertToString();
    }

    @RequestMapping("/book")
    @PostMapping
    public String addBook(@RequestBody AddBookRequest request) {
        return bookShopService.addBook(request).convertToString();
    }

    @RequestMapping("/user/{username}")
    @GetMapping
    public String getUser(@PathVariable String username) {
        return bookShopService.showUserDetails(username).convertToString();
    }

    @RequestMapping("/author/{authorName}")
    @GetMapping
    public String getAuthor(@PathVariable String authorName) {
        return bookShopService.showAuthorDetails(authorName).convertToString();
    }

    @RequestMapping("/book/{bookTitle}")
    @GetMapping
    public String getBook(@PathVariable String bookTitle) {
        return bookShopService.showBookDetails(bookTitle).convertToString();
    }

    @PostMapping("/cart")
    public String addCart(@RequestBody CartRequest request) {
        return bookShopService.addShoppingCart(request).convertToString();
    }

    @DeleteMapping("/cart")
    public String removeCart(@RequestBody CartRequest request) {
        return bookShopService.removeShoppingCart(request).convertToString();
    }

    @PostMapping("/credit")
    public String addCredit(@RequestBody AddCreditRequest request) {
        return bookShopService.addCredit(request).convertToString();
    }

    @PostMapping("/purchase")
    public String finishPurchase(@RequestBody purchaseCartRequest request) {
        return bookShopService.purchaseCart(request).convertToString();
    }

    @PostMapping("/borrow")
    public String borrowBook(@RequestBody BorrowBookRequest request) {
        return bookShopService.borrowBook(request).convertToString();
    }
}
