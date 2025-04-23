package org.example.services;

import org.example.entities.*;
import org.example.request.AddBookRequest;
import org.example.request.BookContentRequest;
import org.example.response.BookContentResponse;
import org.example.response.BookResponses;
import org.example.response.Response;
import org.example.utils.AuthenticationUtils;
import org.example.utils.BookFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static org.example.utils.TimeUtils.isStillInBorrowInterval;

@Service
public class BookService {

    @Autowired
    private BookShop bookShop;

    public void setBookShop(BookShop bookShop) {
        this.bookShop=bookShop;
    }

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

    public Response getBookContent(BookContentRequest request) {
        User user = bookShop.findUser(request.getUsername());
        Book book = bookShop.findBook(request.getTitle());
        Response auth = bookShop.checkUser(AuthenticationUtils.getUsername(), Role.CUSTOMER);
        if (!auth.isSuccess())
            return auth;
        if (book == null) {
            return new Response(false, "Book not exist", null);
        }
        if (!hasBook(user, book)) {
            return new Response(false, "User has not purchased this book", null);
        }
        BookContentResponse response = new BookContentResponse(book.getTitle(), book.getContent(), book.getAuthor().getName());
        return new Response(true, "Book content retrieved successfully.", response);
    }

    public boolean hasBook(User user, Book book)
    {
        for (PurchaseReceipt receipt : bookShop.getReceipts()) {
            if(!receipt.isSuccess())
                continue;
            if(!receipt.getUser().getUsername().equals(user.getUsername()))
                continue;
            if(receipt.getBooks().contains(book))
                return true;
            if(receipt.getBorrowedBooks().containsKey(book))
            {
                LocalDateTime date = receipt.getDate();
                int borrowedDays = receipt.getBorrowedBooks().get(book);

                if (isStillInBorrowInterval(date, borrowedDays)) return true;
            }
        }
        return false;
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

    public BookResponses createTmpBookResponse(Book book) {
        return new BookResponses(book.getTitle(),
                book.getAuthor().getName(), book.getPublisher(),
                book.getGenres(), book.getYear(), book.getPrice());
    }

    public float calculateAverageRating(Book book, List<Review> reviews){
        float number =0;
        float ratingSum =0;
        for (Review r : reviews) {
            if (r.getBookTitle().equals(book.getTitle())) {
                number++;
                ratingSum += r.getRate();
            }
        }
        if(number == 0)
            return 0;
        return ratingSum / number;
    }

    private int getReviewNumber(String bookTitle) {
        return (int) bookShop.getReviews().stream().filter(r -> r.getBookTitle().equals(bookTitle)).count();
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
        if (Objects.equals(bookFilter.getOrder(), "rating")) {
            result.forEach(book ->
                    book.setAverageRating(calculateAverageRating(book, bookShop.getReviews())));
            result.sort(Comparator.comparingDouble(Book::getAverageRating));

        } else if(Objects.equals(bookFilter.getOrder(), "reviewNumber")){
            result.forEach(book ->
                    book.setReviewNumber(getReviewNumber(book.getTitle())));
            result.sort(Comparator.comparingInt(Book::getReviewNumber));
        }

        if(bookFilter.isInverse())
            result = result.reversed();
        int pageSize = bookFilter.getPageSize() <= 0 ? 10 : bookFilter.getPageSize();
        int start = pageSize * bookFilter.getPage();
        int end = Integer.min(result.size(), pageSize * (bookFilter.getPage() + 1));
        return result.subList(start, end);
    }

    private boolean hasFilterCondition(Book book, BookFilter bookFilter) {
        List<String> authors  = Optional.ofNullable(bookFilter.getAuthor())
                .orElse(List.of(book.getAuthor().getName()));
        List<String> titles  = Optional.ofNullable(bookFilter.getTitle())
                .orElse(List.of(book.getTitle()));
        List<String> genres  = Optional.ofNullable(bookFilter.getGenre())
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

    public Response getAllBooks() {
        List<BookResponses> bookResponses = new ArrayList<>();
        for (Book book : bookShop.getBooks()) {
            BookResponses bookResponse = createTmpBookResponse(book);
            bookResponse.setTotalBuy(getCountOfBuy(book));
            bookResponse.setImageLink(book.getImageLink());
            bookResponses.add(bookResponse);
        }
        return new Response(true, "Books retrieved successfully.", bookResponses);
    }

    public int getCountOfBuy(Book book) {
        int count = 0;
        for (PurchaseReceipt receipt : bookShop.getReceipts()) {
            if (receipt.isSuccess() && receipt.getBooks().contains(book)) {
                count++;
            }
        }
        return count;
    }

}
