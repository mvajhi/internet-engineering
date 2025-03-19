package org.example.services;

import org.example.entities.BookShop;
import org.example.entities.Review;
import org.example.request.AddBookRequest;
import org.example.entities.Book;
import org.example.response.BookResponses;
import org.example.utils.AuthenticationUtils;
import org.example.utils.BookFilter;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {

    private BookShop bookShop;

    public BookService(BookShop bookShop){
        this.bookShop = bookShop;
    }

    public BookService(){

    }

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

    public List<Book> paginatedSearch(BookFilter bookFilter) {
        if (!AuthenticationUtils.loggedIn()){
            return null;
        }
        List<Book> result = new ArrayList<>();
        for (Book book : bookShop.getBooks()) {
            if(hasFilterCondition(book, bookFilter)) {
                result.add(book);
            }
        }
        int pageSize = bookFilter.getPageSize() <= 0 ? 10 : bookFilter.getPageSize();
        int start = pageSize * bookFilter.getPage();
        int end = Integer.min(result.size(), pageSize * (bookFilter.getPage() + 1));
        return result.subList(start, end);
    }

    private boolean hasFilterCondition(Book book, BookFilter bookFilter) {
        List<String> authors  = Optional.of(bookFilter.getAuthor())
                .orElse(List.of(book.getAuthor().getName()));
        List<String> titles  = Optional.of(bookFilter.getTitle())
                .orElse(List.of(book.getTitle()));
        List<String> genres  = Optional.of(bookFilter.getGenre())
                .orElse(book.getGenres());

        if (titles.contains(book.getTitle()) && authors.contains(book.getAuthor().getName())) {
            if (bookFilter.getYear() == 0 || bookFilter.getYear() == book.getYear()) {
                if (new HashSet<>(book.getGenres()).containsAll(genres)){
                    return true;
                }
            }
        }
        return false;
    }
}
