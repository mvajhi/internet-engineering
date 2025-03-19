package org.example.controller;

import org.example.request.*;
import org.example.services.BookShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BookShopController {

    @Autowired
    BookShopService bookShopService;

    @RequestMapping("/author")
    @PostMapping
    public String addAuthor(@RequestBody AddAuthorRequest request) {
        return bookShopService.addAuthor(request).convertToString();
    }

    @RequestMapping("/books")
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

    @RequestMapping("/books/{bookTitle}")
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

    @GetMapping("/cart")
    public String getCart(@RequestBody ShowCartRequest request) {
        return bookShopService.showShoppingCart(request).convertToString();
    }

    @PostMapping("/cart/borrow")
    public String borrowBook(@RequestBody BorrowBookRequest request) {
        return bookShopService.borrowBook(request).convertToString();
    }

    @PostMapping("/credit")
    public String addCredit(@RequestBody AddCreditRequest request) {
        return bookShopService.addCredit(request).convertToString();
    }

    @PostMapping("/purchase")
    public String finishPurchase(@RequestBody purchaseCartRequest request) {
        return bookShopService.purchaseCart(request).convertToString();
    }

    @GetMapping("/purchase/books")
    public String getBooks(@RequestBody ShowHistoryRequest request) {
        return bookShopService.showBooks(request).convertToString();
    }

    @GetMapping("/purchase/history")
    public String getCartHistory(@RequestBody ShowHistoryRequest request) {
        return bookShopService.showShoppingCartHistory(request).convertToString();
    }

    @PostMapping("/books/{bookTitle}/review")
    public String addReview(@RequestBody AddReviewRequest request, @PathVariable String bookTitle) {
        request.setTitle(bookTitle);
        return bookShopService.addReview(request).convertToString();
    }

    @GetMapping("/books/{bookTitle}/review")
    public String getReviews(@PathVariable String bookTitle) {
        return bookShopService.showBookReviews(bookTitle).convertToString();
    }
}
