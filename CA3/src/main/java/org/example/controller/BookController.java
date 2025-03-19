package org.example.controller;

import org.example.entities.Book;
import org.example.services.BookService;
import org.example.utils.BookFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping("search")
    public List<Book> search(@RequestParam BookFilter bookFilter) {
        return bookService.paginatedSearch(bookFilter);
    }
}
