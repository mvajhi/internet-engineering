package org.example.repository;

import org.example.entities.Author;
import org.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);
    List<Author> findByUser(User user);
    boolean existsByName(String name);
    
    @Query("SELECT a FROM Author a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(a.penName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Author> searchByNameOrPenName(String keyword);
    
    List<Author> findByNationality(String nationality);
}