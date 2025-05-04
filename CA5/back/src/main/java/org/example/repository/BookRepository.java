package org.example.repository;

import org.example.entities.Author;
import org.example.entities.Book;
import org.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);
    boolean existsByTitle(String title);
    List<Book> findByAuthor(Author author);
    List<Book> findByAdmin(User admin);
    
    @Query("SELECT b FROM Book b JOIN b.genres g WHERE g.name = :genreName")
    List<Book> findByGenreName(String genreName);
    
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> searchByTitle(String keyword);
}