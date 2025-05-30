package org.example.repository;

import org.example.entities.Book;
import org.example.entities.Cart;
import org.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
    Optional<Cart> findByUserUsername(String username);
    
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cart c JOIN c.purchasedBooks b WHERE c.user = :user AND b = :book")
    boolean existsByUserAndPurchasedBooksContains(User user, Book book);
    
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cart c JOIN c.borrowedBooks b WHERE c.user = :user AND KEY(b) = :book")
    boolean existsByUserAndBorrowedBooksContains(User user, Book book);
}