package org.example.response;

import org.example.entities.Book;

import java.util.List;

public class SearchBookResponse {
    public SearchBookResponse(String search, List<Book> books) {
        this.search = search;
        this.books = books;
    }

    String search;
    List<Book> books;
}
