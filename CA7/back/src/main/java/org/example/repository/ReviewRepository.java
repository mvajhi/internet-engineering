package org.example.repository;

import org.example.entities.Book;
import org.example.entities.Review;
import org.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBook(Book book);
    List<Review> findByUser(User user);
    Optional<Review> findByUserAndBook(User user, Book book);
    
    @Query("SELECT AVG(r.rate) FROM Review r WHERE r.book = :book")
    Float findAverageRatingByBook(Book book);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.book = :book")
    Integer countByBook(Book book);
}