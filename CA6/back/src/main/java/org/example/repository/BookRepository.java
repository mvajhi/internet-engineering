package org.example.repository;

import org.example.entities.Author;
import org.example.entities.Book;
import org.example.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT b FROM Book b " +
           "WHERE (:year = 0 OR b.year = :year) " +
           "AND (:title IS NULL OR LOWER(b.title) IN :title) " +
           "AND (:authorName IS NULL OR LOWER(b.author.name) IN :authorName)")
    Page<Book> searchBooks(
            @Param("title") List<String> title,
            @Param("authorName") List<String> authorName,
            @Param("year") int year,
            Pageable pageable
    );

    
    @Query("SELECT b FROM Book b JOIN b.genres g " +
           "WHERE (:year = 0 OR b.year = :year) " +
           "AND (:title IS NULL OR LOWER(b.title) IN (:title)) " +
           "AND (:authorName IS NULL OR LOWER(b.author.name) IN (:authorName)) " +
           "AND (:genreName IS NULL OR g.name IN (:genreName)) " +
           "GROUP BY b")
    Page<Book> searchBooksWithGenres(
        @Param("title") List<String> title,
        @Param("authorName") List<String> authorName,
        @Param("year") int year,
        @Param("genreName") List<String> genreName,
        Pageable pageable
    );
    
    @Query("SELECT AVG(r.rate) FROM Review r WHERE r.book.title = :title")
    Float getAverageRatingByTitle(@Param("title") String title);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.book.title = :title")
    Integer getReviewCountByTitle(@Param("title") String title);
}