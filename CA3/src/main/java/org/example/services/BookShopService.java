package org.example.services;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.request.*;
import org.example.response.*;
import org.example.entities.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.utils.DataLoaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookShopService {
    BookShop bookShop = new BookShop();

    @Autowired
    UserService userService;
    BookService bookService = new BookService();
    AuthorService authorService = new AuthorService();
    ReviewService reviewService = new ReviewService();
    ObjectMapper mapper = new ObjectMapper();

    public BookShopService(UserService userService) {
        this.userService = userService;
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
        Response response = new Response(true, "done", null);
        Author newAuthor = authorService.createAuthor(request);
        if (!bookShop.isAuthorNameUnique(newAuthor.getName())) {
            try {
                response = new Response(false, "redundant author name", null);
            } catch (Exception e) {
                response = new Response(false, "user dont exist", null);
            }
        }
        if (!bookShop.isUserAdmin(request.getUsername())) {
            response = new Response(false, "user is not admin", null);
        }
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
        if (!bookShop.isUserAdmin(request.getUsername())) {
            return new Response(false, "user is not admin", null);
        }
        this.bookShop.addBook(newBook);
        return new Response(true, "Book added successfully", null);

    }

    public Response addShoppingCart(CartRequest request) {
        if (!bookShop.isUserCustomer(request.getUsername())) {
            return new Response(false, "user is admin", null);
        }
        Cart userCart = bookShop.getCartByUsername(request.getUsername());
        if (userCart == null) {
            User user = bookShop.findUser(request.getUsername());
            if (user == null)
                return new Response(false, "invalid username", null);
            userCart = new Cart();
            userCart.setUser(user);
            userCart.setPurchasedBooks(new ArrayList<>());
            this.bookShop.addBasket(userCart);
        }
        Book buiedBook = bookShop.findBook(request.getTitle());
        if (buiedBook == null) {
            return new Response(false, "invalid book title", null);
        }
        if (userCart.getPurchasedBooks().size() > 9) {
            return new Response(false, "book limit exceedes", null);
        }
        userCart.addPurchasedBook(buiedBook);
        return new Response(true, "Added book to cart", null);
    }

    public Response removeShoppingCart(CartRequest request) {
        if (!bookShop.isUserCustomer(request.getUsername())) {
            return new Response(false, "user is admin", null);
        }
        Cart userCart = bookShop.getCartByUsername(request.getUsername());
        if (userCart == null) {
            User user = bookShop.findUser(request.getUsername());
            if (user == null)
                return new Response(false, "invalid username", null);
            return new Response(false, "not in cart.", null);
        }
        Book buiedBook = bookShop.findBook(request.getTitle());
        if (buiedBook == null) {
            return new Response(false, "invalid book title", null);
        }
        if (!userCart.hasPurchasedBook(buiedBook))
            return new Response(false, "not in cart.", null);

        userCart.removePurchasedBook(buiedBook);
        return new Response(true, "Remove book from cart", null);
    }

    public Response borrowBook(BorrowBookRequest request) {
        if (!bookShop.isUserCustomer(request.getUsername())) {
            return new Response(false, "user is admin", null);
        }
        Cart userCart = bookShop.getCartByUsername(request.getUsername());
        if (userCart == null) {
            User user = bookShop.findUser(request.getUsername());
            if (user == null)
                return new Response(false, "invalid username", null);
            userCart = new Cart();
            userCart.setUser(user);
            this.bookShop.addBasket(userCart);
        }
        Book buiedBook = bookShop.findBook(request.getTitle());
        if (buiedBook == null) {
            return new Response(false, "invalid book title", null);
        }
        if (request.getDays() > 9) {
            return new Response(false, "borrow day exceedes", null);
        }
        userCart.addBorrowedBook(buiedBook, request.getDays());
        return new Response(true, "Added borrow book to cart", null);
    }

    public Response addReview(AddReviewRequest request) {
        Review newReview = reviewService.createReview(request);
        User user = bookShop.findUser(request.getUsername());
        Book book = bookShop.findBook(request.getTitle());
        if (user == null) {
            return new Response(false, "User not exist", null);
        }
        if (user.getRole() != Role.CUSTOMER) {
            return new Response(false, "User is not customer", null);
        }
        if (book == null) {
            return new Response(false, "Book not exist", null);
        }
        if (!bookShop.hasBook(user, book)) {
            return new Response(false, "User has not purchased this book", null);
        }
        this.bookShop.addReview(newReview);
        return new Response(true, "Review added successfully", null);
    }

    public Response showUserDetails(String username) {
        try {
            User user = bookShop.findUser(username);
            if (user == null) {
                throw new Exception();
            }
//            TODO : admin does not have credit
            return new Response(true, "User details retrieved successfully.", user);
        } catch (Exception e) {
            return new Response(false, "User not exist", null);
        }
    }

    public Response showAuthorDetails(String name) {
        try {
            Author author = bookShop.findAuthor(name);
            if (author == null) {
                throw new Exception();
            }
            return new Response(true, "Author details retrieved successfully.", author);
        } catch (Exception e) {
            return new Response(false, "Author not exist", null);
        }
    }

    public Response showBookDetails(String title) {
        try {
            Book book = bookShop.findBook(title);
            if (book == null) {
                throw new Exception();
            }
//            TODO : Add avg rating to book
//            TODO : remove content
            return new Response(true, "Book details retrieved successfully", book);
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
            List<Review> reviewsList = this.bookShop.getReviews().stream().filter(e -> e.getBookTitle().equals(title)).toList();
            return new Response(true, "Book reviews retrieved successfully.", mapper.writeValueAsString(reviewsList));
        } catch (Exception e) {
            return new Response(false, "Book not exist", null);
        }
    }

    public Response showBookContent(String username, String title) {
        try {
            User user = bookShop.findUser(username);
            if (user == null) {
                return new Response(false, "User not exist", null);
            }

            Book book = bookShop.findBook(title);
            if (book == null) {
                return new Response(false, "Book not exist", null);
            }

            boolean hasAccess = false;
            List<PurchaseReceipt> userReceipts = this.bookShop.getReceiptsByUsername(username);

            for (int i = 0; i < userReceipts.size(); i++) {
                var receipt = userReceipts.get(i);
                if (receipt.getBooks() != null && receipt.getBooks().stream().anyMatch(b -> b.getTitle().equals(title))) {
                    hasAccess = true;
                    break;
                }

                Map<Book, Integer> borrowedBooks = receipt.getBorrowedBooks();
                if (borrowedBooks.containsKey(book)) {
                    if (receipt.getDate().plusDays(borrowedBooks.get(book)).isAfter(LocalDateTime.now())) {
                        hasAccess = true;
                        break;
                    }
                }
            }

            if (!hasAccess) {
                return new Response(false, "User does not have access to this book", null);
            }

            Map<String, String> content = new HashMap<>();
            content.put("title", book.getTitle());
            content.put("content", "Lorem ipsum..."); // محتوای واقعی کتاب در اینجا قرار می‌گیرد
            return new Response(true, "Book content retrieved successfully.", mapper.writeValueAsString(content));
        } catch (Exception e) {
            return new Response(false, "Error retrieving book content", null);
        }
    }

    public Response addCredit(AddCreditRequest request) {
        if (!bookShop.isUserCustomer(request.getUsername())) {
            return new Response(false, "User Should Be Customer", null);
        }
        if (request.getCredit() < 1000) {
            return new Response(false, "Credit Should Be More Than 1000", null);
        }
        userService.addCredit(bookShop.findUser(request.getUsername()), request.getCredit());
        return new Response(true, "Credit added succesfully", null);
    }

    public Response purchaseCart(purchaseCartRequest request) {
        if (!bookShop.isUserCustomer(request.getUsername())) {
            return new Response(false, "user is admin", null);
        }
        Cart userCart = this.bookShop.getCartByUsername(request.getUsername());
        if (userCart == null) {
            User user = bookShop.findUser(request.getUsername());
            if (user == null)
                return new Response(false, "invalid username", null);
            return new Response(false, "Empty Book List", null);
        }
        PurchaseReceipt receipt = userService.finishPurchase(userCart);
        if (!receipt.isSuccess()) {
            return new Response(false, receipt.getMessage(), null);
        }
        this.bookShop.addRecipt(receipt);
        PurchaseCartResponse purchaseCartResponse = new PurchaseCartResponse();
        purchaseCartResponse.setBookCount(receipt.getBooks().size() + receipt.getBorrowedBooks().size());
        purchaseCartResponse.setTotalCost(receipt.getTotalPrice());
        purchaseCartResponse.setDate(receipt.getDate());

        bookShop.removeCart(userCart);

        return new Response(true, "Purchased completed Succesfully", purchaseCartResponse);
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
        if (!bookShop.isUserExist(username)) {
            return new Response(false, "invalid username", null);
        }
        if (!this.bookShop.isUserCustomer(username)) {
            return new Response(false, "user should be customer", null);
        }
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
}
