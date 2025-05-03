package org.example.config;

import org.example.entities.BookShop;
import org.example.services.BookShopService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookShopConfig {


    @Bean
    public BookShop makeBookShop(){
        return new BookShop();
    }
}
