package org.example.services;

import org.example.entities.Review;
import org.example.request.AddBookRequest;
import org.example.entities.Book;
import org.example.response.BookResponses;

import java.util.List;

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

    public int calculateAverageRating(Book book, List<Review> reviews){
        int number =0;
        int ratingSum =0;
        for (Review r : reviews) {
            if (r.getBookTitle().equals(book.getTitle())) {
                number++;
                ratingSum += r.getRate();
            }
        }
        if(number == 0)
            return -1;
        return ratingSum / number;
    }

    private boolean isBookValid(Book newBook){
        return newBook.getGenres().size() <= 1;
    }
}
