package org.example.services;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.request.*;
import org.example.response.*;
import org.example.entities.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.utils.AuthenticationUtils;
import org.example.utils.DataLoaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.exeptions.*;

import static org.example.services.ReviewService.calculateAverageRating;
import static org.example.utils.TimeUtils.isStillInBorrowInterval;

@Service
public class BookShopService {
    @Autowired
    private BookShop bookShop;

    @Autowired
    UserService userService;
    BookService bookService = new BookService();
    AuthorService authorService = new AuthorService();
    ReviewService reviewService = new ReviewService();
    ObjectMapper mapper = new ObjectMapper();

    public BookShopService(UserService userService, BookShop bookShop) {
        this.userService = userService;
        this.bookShop = bookShop;
        userService.setBookService(bookService);
        bookService.setBookShop(bookShop);
        mapper.registerModule(new JavaTimeModule());
        loadInitialData();
    }

    private void loadInitialData() {
        try {
            DataLoaderUtil dataLoader = new DataLoaderUtil(bookShop);
            dataLoader.loadAllData();
        } catch (Exception e) {
            System.err.println("Error loading initial data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public Response addUser(AddUserRequest request) {
        Response response = new Response(true, "User added successfully.", null);
        User newUser = userService.createUser(request);
        if (newUser == null) {
            return new Response(false, "invalid user parameter", null);
        }
        if (!bookShop.isEmailUnique(newUser.getEmail())) {
            response.setSuccess(false);
            response.setMessage("Email is not unique");
        }
        if (!bookShop.isUsernameUnique(newUser.getUsername())) {
            response.setSuccess(false);
            response.setMessage("username is not unique");
        }
        this.bookShop.addUser(newUser);
        return response;
    }

    public Response addAuthor(AddAuthorRequest request) {
        Response auth = bookShop.checkUser(AuthenticationUtils.getUsername(), Role.ADMIN);
        if (!auth.isSuccess())
            return auth;
            
        Response response = new Response(true, "Author added successfully", null);
        Author newAuthor = authorService.createAuthor(request);
        newAuthor.setImageLink(request.getImageLink());
        
        if (!bookShop.isAuthorNameUnique(newAuthor.getName()))
            return new Response(false, "redundant author name", null);
            
        // Set the current admin user as the Author's user
        String username = AuthenticationUtils.getUsername();
        User adminUser = bookShop.findUser(username);
        if (adminUser == null) {
            return new Response(false, "Admin user not found", null);
        }
        
        newAuthor.setUser(adminUser);
        this.bookShop.addAuthor(newAuthor);

        return response;
    }

    public Response addBook(AddBookRequest request) {
        Book newBook = bookService.createBook(request);
        Author author = bookShop.findAuthor(request.getAuthor());
        if (author == null)
            return new Response(false, "invalid author name", null);
        newBook.setAuthor(author);
        if (!bookShop.isBookNameUnique(newBook.getTitle())) {
            return new Response(false, "book title is not unique", null);
        }
        Response auth = bookShop.checkUser(AuthenticationUtils.getUsername(), Role.ADMIN);
        if (!auth.isSuccess())
            return auth;
        if (newBook.getGenres().isEmpty()) {
            return new Response(false, "book should have at least one genre", null);
        }
        newBook.setImageLink(request.getImageLink());
        newBook.setAdmin(bookShop.findUser(AuthenticationUtils.getUsername()));
        this.bookShop.addBook(newBook);
        return new Response(true, "Book added successfully", null);

    }

    public Response addShoppingCart(CartRequest request) {
        Response auth = bookShop.checkUser(AuthenticationUtils.getUsername(), Role.CUSTOMER);
        if (!auth.isSuccess())
            return auth;
        Cart userCart = bookShop.getCartByUsername(AuthenticationUtils.getUsername());
        if (userCart == null) {
            User user = bookShop.findUser(AuthenticationUtils.getUsername());
            userCart = new Cart();
            userCart.setUser(user);
            userCart.setPurchasedBooks(new ArrayList<>());
            this.bookShop.addBasket(userCart);
        }
        Book buiedBook = bookShop.findBook(request.getTitle());
        if (buiedBook == null) {
            return new Response(false, "invalid book title", null);
        }
        if (userCart.getPurchasedBooks().size() + userCart.getBorrowedBooks().size() > 9) {
            return new Response(false, "book limit exceedes", null);
        }
        userCart.addPurchasedBook(buiedBook);
        
        // Save the updated cart to persist the changes
        this.bookShop.addBasket(userCart);
        
        return new Response(true, "Added book to cart", null);
    }

    public Response removeShoppingCart(CartRequest request) {
        Response auth = bookShop.checkUser(AuthenticationUtils.getUsername(), Role.CUSTOMER);
        if (!auth.isSuccess())
            return auth;

        Cart userCart = bookShop.getCartByUsername(request.getUsername());
        if (userCart == null)
            return new Response(false, "not in cart.", null);

        Book buiedBook = bookShop.findBook(request.getTitle());
        if (buiedBook == null)
            return new Response(false, "invalid book title", null);

        if (!userCart.hasPurchasedBook(buiedBook))
            return new Response(false, "not in cart.", null);

        userCart.removePurchasedBook(buiedBook);
        return new Response(true, "Remove book from cart", null);
    }

    public Response borrowBook(BorrowBookRequest request) {
        Response auth = bookShop.checkUser(AuthenticationUtils.getUsername(), Role.CUSTOMER);
        if (!auth.isSuccess())
            return auth;
        Cart userCart = bookShop.getCartByUsername(AuthenticationUtils.getUsername());
        if (userCart == null) {
            User user = bookShop.findUser(AuthenticationUtils.getUsername());
            userCart = new Cart();
            userCart.setUser(user);
            this.bookShop.addBasket(userCart);
        }

        Book buiedBook = bookShop.findBook(request.getTitle());
        if (buiedBook == null)
            return new Response(false, "invalid book title", null);
        if (request.getDays() > 9)
            return new Response(false, "borrow day exceedes", null);
        if (request.getDays() < 1)
            return new Response(false, "invalid borrow day", null);
        userCart.addBorrowedBook(buiedBook, request.getDays());
        
        // Save the updated cart to persist the changes
        this.bookShop.addBasket(userCart);
        
        return new Response(true, "Added borrow book to cart", null);
    }

    public Response addReview(AddReviewRequest request) {
        Review newReview = reviewService.createReview(request);
        User user = bookShop.findUser(AuthenticationUtils.getUsername());
        Book book = bookShop.findBook(request.getTitle());
        Response auth = bookShop.checkUser(AuthenticationUtils.getUsername(), Role.CUSTOMER);
        if (!auth.isSuccess())
            return auth;
        if (book == null)
            return new Response(false, "Book not exist", null);
        if (!bookService.hasBook(user, book))
            return new Response(false, "User has not purchased this book", null);
        if (newReview.getRate() < 1 || newReview.getRate() > 5)
            return new Response(false, "invalid rate", null);
        newReview.setBook(book);
        newReview.setUser(user);
        this.bookShop.addReview(newReview);
        book.setAverageRating(calculateAverageRating(book, bookShop.getReviews()));
        return new Response(true, "Review added successfully", null);
    }

    public Response showUserDetails(String username) {
        User user = bookShop.findUser(username);
        if (user == null)
            return new Response(false, "User not exist", null);

        return new Response(true, "User details retrieved successfully.", user);
    }

    public Response showAuthorDetails(String name) {
        Author author = bookShop.findAuthor(name);
        if (author == null)
            return new Response(false, "Author not exist", null);
        return new Response(true, "Author details retrieved successfully.", author);
    }

    public Response showBookDetails(String title) {
        try {
            Book book = bookShop.findBook(title);
            if (book == null) {
                throw new Exception();
            }

            float average = bookService.calculateAverageRating(book, bookShop.getReviews());
            book.setAverageRating(average);
            return new Response(true, "Book details retrieved successfully",
                    BookDetailsResponse.makeDetailedFromBook(book, average));
        } catch (Exception e) {
            return new Response(false, "Book not exist", null);
        }
    }

    public Response showBookReviews(String title) {
        try {
            Book book = bookShop.findBook(title);
            if (book == null) {
                throw new Exception();
            }
            List<Review> reviewsList = bookShop.findReviews(title);
            float average = bookService.calculateAverageRating(book, bookShop.getReviews());
            ShowReviewResponse showReviewResponse = new ShowReviewResponse(reviewsList, average);
            return new Response(true, "Book reviews retrieved successfully.", showReviewResponse);
        } catch (Exception e) {
            return new Response(false, "Book not exist", null);
        }
    }

    public Response addCredit(AddCreditRequest request) {
        Response auth = bookShop.checkUser(AuthenticationUtils.getUsername(), Role.CUSTOMER);
        if (!auth.isSuccess())
            return auth;
        if (request.getCredit() < 1000) {
            return new Response(false, "Credit Should Be More Than 1000", null);
        }
        userService.addCredit(bookShop.findUser(AuthenticationUtils.getUsername()), request.getCredit());
        return new Response(true, "Credit added succesfully", null);
    }

    public Response purchaseCart(purchaseCartRequest request) {
        Response auth = bookShop.checkUser(AuthenticationUtils.getUsername(), Role.CUSTOMER);
        if (!auth.isSuccess())
            return auth;
        Cart userCart = this.bookShop.getCartByUsername(request.getUsername());
        if (userCart == null)
            return new Response(false, "Empty Book List", null);

        PurchaseReceipt receipt = userService.finishPurchase(userCart);
        if (!receipt.isSuccess())
            return new Response(false, receipt.getMessage(), null);
        this.bookShop.addRecipt(receipt);
        PurchaseCartResponse purchaseCartResponse = new PurchaseCartResponse();
        purchaseCartResponse.setBookCount(receipt.getBooks().size() + receipt.getBorrowedBooks().size());
        purchaseCartResponse.setTotalCost(receipt.getTotalPrice());
        purchaseCartResponse.setDate(receipt.getDate());

        bookShop.removeCart(userCart);

        return new Response(true, "Purchased completed Successfully", purchaseCartResponse);
    }

    public Response searchBookByTitle(String title) {
        String key = title.toLowerCase();
        List<Book> books = this.bookShop.getBooks().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(key))
                .toList();
        return new Response(true, "Books containing '" + title + "' in their title",
                new SearchBookResponse(title, books));
    }

    public Response searchBookByAuthor(String authorName) {
        String key = authorName.toLowerCase();
        List<Book> books = this.bookShop.getBooks().stream()
                .filter(book -> book.getAuthor().getName().toLowerCase().contains(key))
                .toList();
        return new Response(true, "Books by '" + authorName + "'",
                new SearchBookResponse(authorName, books));
    }

    public Response searchBookByGenre(String genre) {
        List<Book> books = this.bookShop.getBooks().stream().filter(book -> bookService.isGenreIs(book, genre)).toList();
        return new Response(true, "Books in the " + genre + " genre", new SearchBookResponse(genre, books));
    }

    public Response searchBookByYear(String fromYear, String toYear) {
        int from = Integer.parseInt(fromYear);
        int to = Integer.parseInt(toYear);
        if (to < from) {
            return new Response(false, "invalid dates", null);
        }
        List<Book> books = this.bookShop.getBooks().stream().filter(book -> bookService.isPublishedIn(book, from, to)).toList();
        return new Response(true, "Books published from " + from + " to " + to + ":", new SearchBookResponse(from + "-" + to, books));
    }

    public Response showPurchasedBooks(String username) {
        Response auth = bookShop.checkUser(AuthenticationUtils.getUsername(), Role.CUSTOMER);
        if (!auth.isSuccess())
            return auth;
        List<PurchaseReceipt> purchaseReceipts = this.bookShop.getReceiptsByUsername(username);
        List<ShowPurchasedBookResponse> responses = this.findPurchasedBooks(purchaseReceipts);
        return new Response(true, "Purchase history retrieved successfully.", responses);
    }

    private List<ShowPurchasedBookResponse> findPurchasedBooks(List<PurchaseReceipt> receipts) {
        List<ShowPurchasedBookResponse> responses = new ArrayList<>();
        for (PurchaseReceipt r : receipts) {
            if (r.getBooks() != null) {
                responses.addAll(r.getBooks().stream().map(b -> createResponseFromBook(b.getTitle())).toList());
            }
            Map<Book, Integer> borrowedBook = r.getBorrowedBooks();
            if (borrowedBook == null) {
                continue;
            }
            for (Book book : borrowedBook.keySet()) {
                if (r.getDate().plusDays(borrowedBook.get(book)).isAfter(LocalDateTime.now())) {
                    ShowPurchasedBookResponse borrowedBookResponse = createResponseFromBook(book.getTitle());
                    borrowedBookResponse.setIsBorrowed(true);
                    responses.add(borrowedBookResponse);

                }
            }
        }
        return responses;
    }

    private ShowPurchasedBookResponse createResponseFromBook(String title) {
        Book book = bookShop.findBook(title);
        ShowPurchasedBookResponse response = new ShowPurchasedBookResponse();
        response.setCategory(book.getGenres());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor().getName());
        response.setPublisher(book.getPublisher());
        response.setCategory(book.getGenres());
        response.setYear(book.getYear());
        response.setPrice(book.getPrice());
        response.setIsBorrowed(false);
        return response;
    }

    private int calculateBorrowPrice(int originalPrice, int days) {
        return (int) (originalPrice * 0.1 * days);
    }

    public Response showShoppingCart(ShowCartRequest request) {
        Response auth = bookShop.checkUser(AuthenticationUtils.getUsername(), Role.CUSTOMER);
        if (!auth.isSuccess())
            return auth;
        Cart userCart = bookShop.getCartByUsername(request.getUsername());
        if (userCart == null) {
            User user = bookShop.findUser(request.getUsername());
            userCart = new Cart();
            userCart.setUser(user);
            userCart.setPurchasedBooks(new ArrayList<>());
            this.bookShop.addBasket(userCart);
        }
        return new Response(true, "Buy cart retrieved successfully", userService.showShoppingCart(userCart));
    }

    public Response showShoppingCartHistory(ShowHistoryRequest request) {
        Response auth = bookShop.checkUser(AuthenticationUtils.getUsername(), Role.CUSTOMER);
        if (!auth.isSuccess())
            return auth;
        User user = bookShop.findUser(request.getUsername());
        BuyHistoryResponse buyHistoryResponse = new BuyHistoryResponse();
        buyHistoryResponse.setUsername(user.getUsername());
        List<PurchaseHistoryResponse> purchaseHistoryResponses = new ArrayList<>();

        for (PurchaseReceipt receipt : bookShop.getReceiptsByUsername(user.getUsername())) {
            List<BookResponses> books=userService.getBookResponsesList(receipt.getBooks(), receipt.getBorrowedBooks());
            purchaseHistoryResponses.add(new PurchaseHistoryResponse(receipt.getDate(), receipt.getTotalPrice(), books));
        }

        buyHistoryResponse.setPurchasesHistory(purchaseHistoryResponses);
        return new Response(true, "Buy cart retrieved successfully", buyHistoryResponse);
    }

    public Response showBooks() {
        Response auth = bookShop.checkUser(AuthenticationUtils.getUsername(), Role.CUSTOMER);
        if (!auth.isSuccess())
            return auth;
        User user = bookShop.findUser(AuthenticationUtils.getUsername());
        List<BookResponses> books = new ArrayList<>();
        for (PurchaseReceipt receipt : bookShop.getReceiptsByUsername(user.getUsername())) {
            books.addAll(userService.getBookResponsesList(receipt.getBooks()));
            for (Map.Entry<Book, Integer> entry : receipt.getBorrowedBooks().entrySet()) {
                if (isStillInBorrowInterval(receipt.getDate(), entry.getValue())) {
                    books.add(userService.getBookResponse(entry, receipt.getDate()));
                }
            }
        }

        UserBookResponse userBookResponse = new UserBookResponse(user.getUsername(), books);
        return new Response(true, "Books retrieved successfully", userBookResponse);
    }

    public Response getAllBooks() {
        return bookService.getAllBooks();
    }

    public Response getAllAuthors() {
        return new Response(true, "Authors retrieved successfully", bookShop.getAuthors());
    }

    public Response showAuthorBooks(String authorName) {
        Author author = bookShop.findAuthor(authorName);
        if (author == null)
            return new Response(false, "Author not exist", null);
        List<Book> books = bookShop.getBooksByAuthor(authorName);
        return new Response(true, "Books by " + authorName + " retrieved successfully", books);
    }
}
