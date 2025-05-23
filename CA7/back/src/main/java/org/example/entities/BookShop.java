package org.example.entities;

import org.example.repository.*;
import org.example.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BookShop {
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final CartRepository cartRepository;
    private final PurchaseReceiptRepository receiptRepository;
    private final WalletRepository walletRepository;
    private final AddressRepository addressRepository;
    private final GenreRepository genreRepository;

    @Autowired
    public BookShop(
            UserRepository userRepository,
            AuthorRepository authorRepository,
            BookRepository bookRepository,
            ReviewRepository reviewRepository,
            CartRepository cartRepository,
            PurchaseReceiptRepository receiptRepository,
            WalletRepository walletRepository,
            AddressRepository addressRepository,
            GenreRepository genreRepository) {
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
        this.cartRepository = cartRepository;
        this.receiptRepository = receiptRepository;
        this.walletRepository = walletRepository;
        this.addressRepository = addressRepository;
        this.genreRepository = genreRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<Author> getAuthors() {
        return authorRepository.findAll();
    }

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    public List<Review> getReviews() {
        return reviewRepository.findAll();
    }

    public List<Cart> getBaskets() {
        return cartRepository.findAll();
    }

    public List<PurchaseReceipt> getReceipts() {
        return receiptRepository.findAll();
    }

    @Transactional
    public void addUser(User user) {
        // First save the Address if it doesn't already exist
        Address address = user.getAddress();
        if (address != null && address.getId() == null) {
            // Check if we already have this address in database to avoid duplicates
            Address existingAddress = addressRepository.findByCountryAndCity(
                    address.getCountry(), address.getCity());
            
            if (existingAddress != null) {
                user.setAddress(existingAddress);
            } else {
                addressRepository.save(address);
            }
        }
        
        // Now save the User
        userRepository.save(user);
        
        // Create wallet for new user if not admin
        if (user.getRole() == Role.CUSTOMER) {
            Wallet wallet = new Wallet(user, BigDecimal.valueOf(user.getBalance() != null ? user.getBalance() : 0));
            walletRepository.save(wallet);
            user.setWallet(wallet);
            userRepository.save(user);  // Save user again to update the wallet reference
        }
    }

    @Transactional
    public void addAuthor(Author author) {
        // First check if the user exists and is saved to the database
        User user = author.getUser();
        if (user == null) {
            throw new IllegalArgumentException("Author must be associated with a User");
        }
        
        if (user.getId() == null) {
            // The user hasn't been saved yet
            // Check if this user exists in the database
            Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
            
            if (existingUser.isPresent()) {
                // Use the existing user from the database
                author.setUser(existingUser.get());
            } else {
                // Save the new user first
                addUser(user); // This will handle saving address and wallet too
            }
        }
        
        // Now save the author
        authorRepository.save(author);
    }

    @Transactional
    public void addBook(Book book) {
        // Process genres before saving the book
        if (book.getGenres() != null && !book.getGenres().isEmpty()) {
            // Create a set to hold the Genre entities
            java.util.Set<Genre> genreEntities = new java.util.HashSet<>();
            
            // For each genre name in the book's genres list
            for (String genreName : book.getGenres()) {
                // Try to find an existing genre with this name
                Optional<Genre> existingGenre = genreRepository.findByName(genreName);
                
                Genre genre;
                if (existingGenre.isPresent()) {
                    // Use the existing genre
                    genre = existingGenre.get();
                } else {
                    // Create and save a new genre
                    genre = new Genre(genreName);
                    genreRepository.save(genre);
                }
                
                // Add the genre entity to our set
                genreEntities.add(genre);
            }
            
            // Set the genres on the book
            book.setGenreEntities(genreEntities);
        }
        
        // Save the book with its genres
        bookRepository.save(book);
    }

    @Transactional
    public void addBasket(Cart cart) {
        cartRepository.save(cart);
    }

    @Transactional
    public void addRecipt(PurchaseReceipt receipt) {
        receiptRepository.save(receipt);
    }

    @Transactional
    public void addReview(Review review) {
        // Remove existing review if it exists
        reviewRepository.findByUserAndBook(review.getUser(), review.getBook())
                .ifPresent(existingReview -> reviewRepository.delete(existingReview));
        
        // Save the new review
        reviewRepository.save(review);
    }

    @Transactional
    public void removeCart(Cart cart) {
        cartRepository.delete(cart);
    }

    public Cart getCartByUsername(String username) {
        return cartRepository.findByUserUsername(username).orElse(null);
    }

    public List<PurchaseReceipt> getReceiptsByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(receiptRepository::findByUser).orElse(List.of());
    }

    public boolean isEmailUnique(String email) {
        return !userRepository.existsByEmail(email);
    }

    public boolean isUserExist(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean isUsernameUnique(String username) {
        return !userRepository.existsByUsername(username);
    }

    public boolean isAuthorNameUnique(String name) {
        return !authorRepository.existsByName(name);
    }

    public boolean isUserAdmin(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(u -> u.getRole() == Role.ADMIN).orElse(false);
    }

    public Response checkUser(String username, Role role) {
        if (username == null)
            return new Response(false, "No users logged in.", null);

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return new Response(false, "invalid username", null);
        }

        if (role == null || user.get().getRole().equals(role)) {
            return new Response(true, "", null);
        }
        
        return new Response(false, "user is " + (role.equals(Role.ADMIN) ? "not " : "") + "admin", null);
    }

    public boolean isUserCustomer(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(u -> u.getRole() == Role.CUSTOMER).orElse(false);
    }

    public boolean isBookNameUnique(String title) {
        return !bookRepository.existsByTitle(title);
    }

    public Book findBook(String title) {
        return bookRepository.findByTitle(title).orElse(null);
    }

    public Author findAuthor(String name) {
        return authorRepository.findByName(name).orElse(null);
    }

    // Load user with their wallet data
    public User findUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        setWalletIfExist(user);
        return user.orElse(null);
    }

    public User findUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        setWalletIfExist(user);
        return user.orElse(null);
    }

    public List<Review> findReviews(String title) {
        Optional<Book> book = bookRepository.findByTitle(title);
        return book.map(reviewRepository::findByBook).orElse(List.of());
    }

    public List<Book> getBooksByAuthor(String authorName) {
        Optional<Author> author = authorRepository.findByName(authorName);
        return author.map(bookRepository::findByAuthor).orElse(List.of());
    }

    private void setWalletIfExist(Optional<User> user) {
        if (user.isPresent() && user.get().getRole() == Role.CUSTOMER) {
            walletRepository.findByUser(user.get()).ifPresent(wallet -> {
                user.get().setWallet(wallet);
            });
        }
    }
}
