package org.example.repository;

import org.example.entities.Book;
import org.example.entities.Order;
import org.example.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);
    List<OrderItem> findByBook(Book book);
    
    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.book = :book")
    Integer countTotalSoldByBook(Book book);
}