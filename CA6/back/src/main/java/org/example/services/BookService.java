package org.example.services;

import org.example.entities.*;
import org.example.repository.BookRepository;
import org.example.request.AddBookRequest;
import org.example.request.BookContentRequest;
import org.example.response.BookCardResponses;
import org.example.response.BookContentResponse;
import org.example.response.BookResponses;
import org.example.response.Response;
import org.example.utils.AuthenticationUtils;
import org.example.utils.BookFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static org.example.utils.TimeUtils.isStillInBorrowInterval;

@Service
public class BookService {

    @Autowired
    private BookShop bookShop;

    @Autowired
    private BookRepository bookRepository;

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

    public List<BookCardResponses> paginatedSearch(BookFilter bookFilter) {
        if (!AuthenticationUtils.loggedIn()){
            return null;
        }
        
        int pageSize = bookFilter.getPageSize() <= 0 ? 10 : bookFilter.getPageSize();
        int page = bookFilter.getPage();
        String sortBy = bookFilter.getSortBy();
        boolean isInverse = bookFilter.isInverse();

        try {
            // Special case for rating and reviewNumber sorting - fetch ALL books and sort in memory
            if ("rating".equals(sortBy) || "reviewNumber".equals(sortBy)) {
                return handleSpecialSorting(bookFilter, sortBy, isInverse, page, pageSize);
            }

            // For regular sorting fields, use database sorting
            return handleRegularSorting(bookFilter, sortBy, isInverse, page, pageSize);
        } catch (Exception e) {
            // Log the error and return empty list instead of crashing
            System.err.println("Error in paginatedSearch: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    private List<BookCardResponses> handleSpecialSorting(BookFilter bookFilter, String sortBy, 
                                                        boolean isInverse, int page, int pageSize) {
        // For rating and reviewNumber sorting, we need to fetch all books and sort in memory
        List<Book> allBooks = bookRepository.findAll();
        
        // Filter the books if any filter criteria are provided
        List<Book> filteredBooks = allBooks;
        if ((bookFilter.getTitle() != null && !bookFilter.getTitle().isEmpty()) || 
            (bookFilter.getAuthor() != null && !bookFilter.getAuthor().isEmpty()) ||
            (bookFilter.getGenre() != null && !bookFilter.getGenre().isEmpty()) ||
            bookFilter.getYear() != 0) {
            
            filteredBooks = allBooks.stream()
                .filter(book -> hasFilterCondition(book, bookFilter))
                .toList();
        }
        
        // Convert to BookCardResponses with ratings
        List<BookCardResponses> results = new ArrayList<>();
        Map<String, Integer> reviewCounts = new HashMap<>();
        
        for (Book book : filteredBooks) {
            BookCardResponses response = new BookCardResponses();
            response.setTitle(book.getTitle());
            response.setAuthor(book.getAuthor().getName());
            response.setPrice(book.getPrice());
            response.setImageLink(book.getImageLink());
            
            // Get the average rating
            Float avgRating = bookRepository.getAverageRatingByTitle(book.getTitle());
            response.setAverageRating(avgRating != null ? avgRating : 0);
            
            // For review count sorting, populate the map
            if ("reviewNumber".equals(sortBy)) {
                Integer reviewCount = bookRepository.getReviewCountByTitle(book.getTitle());
                reviewCounts.put(book.getTitle(), reviewCount != null ? reviewCount : 0);
            }
            
            results.add(response);
        }
        
        // Sort based on the requested field
        if ("rating".equals(sortBy)) {
            results.sort(Comparator.comparing(BookCardResponses::getAverageRating));
        } else if ("reviewNumber".equals(sortBy)) {
            results.sort(Comparator.comparing(book -> reviewCounts.getOrDefault(book.getTitle(), 0)));
        }
        
        // Apply inverse if needed
        if (isInverse) {
            Collections.reverse(results);
        }
        
        // Apply pagination
        int start = page * pageSize;
        int end = Math.min(start + pageSize, results.size());
        
        if (start < results.size()) {
            return results.subList(start, end);
        } else {
            return new ArrayList<>();
        }
    }
    
    private List<BookCardResponses> handleRegularSorting(BookFilter bookFilter, String sortBy, 
                                                       boolean isInverse, int page, int pageSize) {
        // Create sort for database query
        Sort sort;
        if (sortBy == null) {
            sort = Sort.by("title"); // Default sort option
        } else {
            sort = Sort.by(sortBy);
        }
        
        if (isInverse) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        
        // Execute the appropriate query based on filter
        Page<Book> bookPage;
        if ((bookFilter.getTitle() == null || bookFilter.getTitle().isEmpty()) && 
            (bookFilter.getAuthor() == null || bookFilter.getAuthor().isEmpty()) &&
            (bookFilter.getGenre() == null || bookFilter.getGenre().isEmpty()) &&
            bookFilter.getYear() == 0) {
            
            // Get all books with basic pagination
            bookPage = bookRepository.findAll(pageable);
        } 
        else if (bookFilter.getGenre() != null && !bookFilter.getGenre().isEmpty()) {
            bookPage = bookRepository.searchBooksWithGenres(
                bookFilter.getTitle(),
                bookFilter.getAuthor(),
                bookFilter.getYear(),
                bookFilter.getGenre(),
                pageable
            );
        } else {
            bookPage = bookRepository.searchBooks(
                bookFilter.getTitle(),
                bookFilter.getAuthor(),
                bookFilter.getYear(),
                pageable
            );
        }
        
        // Convert to BookCardResponses
        List<Book> books = bookPage.getContent();
        List<BookCardResponses> result = new ArrayList<>();
        
        for (Book book : books) {
            BookCardResponses response = new BookCardResponses();
            response.setTitle(book.getTitle());
            response.setAuthor(book.getAuthor().getName());
            response.setPrice(book.getPrice());
            response.setImageLink(book.getImageLink());
            
            // Get the average rating
            Float avgRating = bookRepository.getAverageRatingByTitle(book.getTitle());
            response.setAverageRating(avgRating != null ? avgRating : 0);
            
            result.add(response);
        }
        
        return result;
    }

    private boolean hasFilterCondition(Book book, BookFilter bookFilter) {
        // Check title filter
        if (bookFilter.getTitle() != null && !bookFilter.getTitle().isEmpty()) {
            boolean hasTitle = false;
            for (String titleFilter : bookFilter.getTitle()) {
                if (book.getTitle().toLowerCase().contains(titleFilter.toLowerCase())) {
                    hasTitle = true;
                    break;
                }
            }
            if (!hasTitle) return false;
        }
        
        // Check author filter
        if (bookFilter.getAuthor() != null && !bookFilter.getAuthor().isEmpty()) {
            boolean hasAuthor = false;
            for (String authorFilter : bookFilter.getAuthor()) {
                if (book.getAuthor().getName().toLowerCase().contains(authorFilter.toLowerCase())) {
                    hasAuthor = true;
                    break;
                }
            }
            if (!hasAuthor) return false;
        }
        
        // Check year filter
        if (bookFilter.getYear() != 0 && book.getYear() != bookFilter.getYear()) {
            return false;
        }
        
        // Check genre filter
        if (bookFilter.getGenre() != null && !bookFilter.getGenre().isEmpty()) {
            boolean hasGenre = false;
            for (String genreFilter : bookFilter.getGenre()) {
                if (book.getGenres().stream().anyMatch(g -> g.equalsIgnoreCase(genreFilter))) {
                    hasGenre = true;
                    break;
                }
            }
            if (!hasGenre) return false;
        }
        
        return true;
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
