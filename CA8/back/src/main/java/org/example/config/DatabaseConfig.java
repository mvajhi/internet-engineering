package org.example.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableJpaRepositories(basePackages = "org.example.repository")
@EntityScan(basePackages = "org.example.entities")
@EnableTransactionManagement
public class DatabaseConfig {

    @Bean
    public CommandLineRunner checkDatabaseConnection(JdbcTemplate jdbcTemplate) {
        return args -> {
            try {
                // Execute a simple query to check database connectivity
                String dbName = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
                System.out.println("✅ Successfully connected to database: " + dbName);
                
                // Check if Wallet table exists
                Integer tableCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'Wallet'", 
                    Integer.class);
                    
                if (tableCount > 0) {
                    System.out.println("✅ Wallet table found in database");
                } else {
                    System.out.println("⚠️ Wallet table not found in database - might need initialization");
                }
                
            } catch (Exception e) {
                System.err.println("❌ Database connection failed: " + e.getMessage());
                // Rethrow to prevent application startup
                throw new RuntimeException("Database connection failed", e);
            }
        };
    }
}