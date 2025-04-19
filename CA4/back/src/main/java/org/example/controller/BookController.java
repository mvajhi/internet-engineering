package org.example.controller;

import org.example.entities.Book;
import org.example.request.BookContentRequest;
import org.example.services.BookService;
import org.example.services.BookShopService;
import org.example.utils.BookFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping("/search")
    public List<Book> search(@ModelAttribute BookFilter bookFilter) {
        return bookService.paginatedSearch(bookFilter);
    }

    @GetMapping("/{bookTitle}/content")
    public String getBookContent(@PathVariable String bookTitle, @RequestBody BookContentRequest request) {
        request.setTitle(bookTitle);
        return bookService.getBookContent(request).convertToString();
    }
}
