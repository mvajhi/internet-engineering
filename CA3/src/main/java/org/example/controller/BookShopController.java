package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.request.AddAuthorRequest;
import org.example.request.AddBookRequest;
import org.example.request.AddUserRequest;
import org.example.services.BookShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
}
