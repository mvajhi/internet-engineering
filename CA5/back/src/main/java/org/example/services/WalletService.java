package org.example.services;

import org.example.entities.User;
import org.example.entities.Wallet;
import org.example.repository.UserRepository;
import org.example.repository.WalletRepository;
import org.example.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletService {
    
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public WalletService(WalletRepository walletRepository, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getBalance(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getWallet() != null) {
            return user.getWallet().getBalance();
        }
        
        return walletRepository.findByUser(user)
                .map(Wallet::getBalance)
                .orElse(BigDecimal.ZERO);
    }
    
    @Transactional
    public Response addBalance(String username, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new Response(false, "Amount must be positive", null);
        }
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Wallet wallet = walletRepository.findByUser(user)
                .orElseGet(() -> {
                    Wallet newWallet = new Wallet(user, BigDecimal.ZERO);
                    user.setWallet(newWallet);
                    return walletRepository.save(newWallet);
                });
        
        wallet.addBalance(amount);
        walletRepository.save(wallet);
        userRepository.save(user); // Update the user to ensure the wallet reference is saved
        
        return new Response(true, "Balance added successfully", wallet.getBalance());
    }
    
    @Transactional
    public Response deductBalance(String username, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new Response(false, "Amount must be positive", null);
        }
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Optional<Wallet> walletOpt = walletRepository.findByUser(user);
        if (walletOpt.isEmpty()) {
            return new Response(false, "Wallet not found", null);
        }
        
        Wallet wallet = walletOpt.get();
        if (wallet.getBalance().compareTo(amount) < 0) {
            return new Response(false, "Insufficient balance", wallet.getBalance());
        }
        
        boolean success = wallet.deductBalance(amount);
        if (success) {
            walletRepository.save(wallet);
            return new Response(true, "Balance deducted successfully", wallet.getBalance());
        } else {
            return new Response(false, "Failed to deduct balance", wallet.getBalance());
        }
    }

    @Transactional
    public Response createWalletIfNotExists(User user) {
        if (user == null) {
            return new Response(false, "User cannot be null", null);
        }
        
        if (walletRepository.findByUser(user).isPresent()) {
            return new Response(false, "Wallet already exists for this user", null);
        }
        
        Wallet wallet = new Wallet(user, BigDecimal.ZERO);
        walletRepository.save(wallet);
        user.setWallet(wallet);
        userRepository.save(user);
        
        return new Response(true, "Wallet created successfully", null);
    }
    
    @Transactional(readOnly = true)
    public void syncUserBalance(User user) {
        if (user == null || user.getRole() == org.example.entities.Role.ADMIN) {
            return;
        }
        
        Optional<Wallet> wallet = walletRepository.findByUser(user);
        wallet.ifPresent(user::setWallet);
    }
}