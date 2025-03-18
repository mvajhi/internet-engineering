package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.request.AddAuthorRequest;
import org.example.request.AddUserRequest;
import org.example.services.BookShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookShopController {

    BookShopService bookShopService = new BookShopService();

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
}
