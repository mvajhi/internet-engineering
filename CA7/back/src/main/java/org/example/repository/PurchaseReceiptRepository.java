package org.example.repository;

import org.example.entities.PurchaseReceipt;
import org.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseReceiptRepository extends JpaRepository<PurchaseReceipt, Long> {
    List<PurchaseReceipt> findByUser(User user);
    List<PurchaseReceipt> findByDateBetween(LocalDateTime start, LocalDateTime end);
    List<PurchaseReceipt> findByUserAndDateBetween(User user, LocalDateTime start, LocalDateTime end);
    List<PurchaseReceipt> findByIsSuccess(boolean isSuccess);
}