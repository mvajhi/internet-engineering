package org.example.services;

import org.example.request.AddBookRequest;
import org.example.entities.Book;
import org.example.response.BookResponses;

public class BookService {

    public Book createBook(AddBookRequest request) {
        Book newBook = new Book();
        newBook.setGenres(request.getGenres());
        newBook.setContent(request.getContent());
        newBook.setTitle(request.getTitle());
        newBook.setPrice(request.getPrice());
        newBook.setYear(request.getYear());
        newBook.setSynopsis(request.getSynopsis());
        newBook.setPublisher(request.getPublisher());
        return newBook;
    }

    public boolean isTitleHas(Book book,String title) {
        return book.getTitle().contains(title);
    }

    public boolean isAuthorNameHas(Book book, String name) {
        return book.getAuthor().getName().contains(name);
    }

    public boolean isGenreIs(Book book, String genre) {
        return book.getGenres().contains(genre);
    }

    public boolean isPublishedIn(Book book, int from, int to) {
        int publishedYear = book.getYear();
        return publishedYear >= from && publishedYear <= to;
    }

    public BookResponses createBookResponse(Book book) {
        return new BookResponses(book.getTitle(),
                book.getAuthor().getName(), book.getPublisher(),
                book.getGenres(), book.getYear(), book.getPrice());
    }

    private boolean isBookValid(Book newBook){
        return newBook.getGenres().size() <= 1;
    }
}
