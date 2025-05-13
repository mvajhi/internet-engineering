package org.example.config;

import org.example.entities.BookShop;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookShopConfig {
    
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final CartRepository cartRepository;
    private final PurchaseReceiptRepository purchaseReceiptRepository;
    private final WalletRepository walletRepository;
    private final AddressRepository addressRepository;
    private final GenreRepository genreRepository;

    @Autowired
    public BookShopConfig(
            UserRepository userRepository,
            AuthorRepository authorRepository,
            BookRepository bookRepository,
            ReviewRepository reviewRepository,
            CartRepository cartRepository,
            PurchaseReceiptRepository purchaseReceiptRepository,
            WalletRepository walletRepository,
            AddressRepository addressRepository,
            GenreRepository genreRepository) {
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
        this.cartRepository = cartRepository;
        this.purchaseReceiptRepository = purchaseReceiptRepository;
        this.walletRepository = walletRepository;
        this.addressRepository = addressRepository;
        this.genreRepository = genreRepository;
    }

    @Bean
    public BookShop makeBookShop() {
        return new BookShop(
            userRepository,
            authorRepository,
            bookRepository,
            reviewRepository,
            cartRepository,
            purchaseReceiptRepository,
            walletRepository,
            addressRepository,
            genreRepository
        );
    }
}
