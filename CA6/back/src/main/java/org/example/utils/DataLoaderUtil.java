package org.example.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class DataLoaderUtil {
    private final BookShop bookShop;

    public DataLoaderUtil(BookShop bookShop) {
        this.bookShop = bookShop;
    }

    public void loadAllData() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String baseUrl = "http://194.60.231.242:8001";

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            try {
                loadData(restTemplate, baseUrl, mapper, "users", "Users", this::createUser);
            } catch (Exception e) {
                ;
            }
            try {
                loadData(restTemplate, baseUrl, mapper, "authors", "Authors", this::createAuthor);
            } catch (Exception e) {
                ;
            }
            try {
                loadData(restTemplate, baseUrl, mapper, "books", "Books", this::createBook);
            } catch (Exception e) {
                ;
            }
            try {
                loadData(restTemplate, baseUrl, mapper, "reviews", "Reviews", this::createReview);
            } catch (Exception e) {
                ;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load initial data", e);
        }
    }

    private <T> void loadData(RestTemplate restTemplate, String baseUrl, ObjectMapper mapper,
                              String endpoint, String entityType,
                              DataProcessor<Map<String, Object>, T> processor) throws JsonProcessingException {
        String jsonData = restTemplate.getForObject(baseUrl + "/" + endpoint, String.class);
        if (jsonData != null) {
            List<Map<String, Object>> dataList = mapper.readValue(jsonData,
                    mapper.getTypeFactory().constructCollectionType(List.class, Map.class));

            for (Map<String, Object> data : dataList) {
                T entity = processor.process(data);
                addToBookShop(entity);
            }
            System.out.println(entityType + " loaded successfully");
        }
    }

    @FunctionalInterface
    private interface DataProcessor<I, O> {
        O process(I input);
    }

    private <T> void addToBookShop(T entity) {
        if (entity instanceof Book) {
            bookShop.addBook((Book) entity);
        } else if (entity instanceof Author) {
            bookShop.addAuthor((Author) entity);
        } else if (entity instanceof User) {
            bookShop.addUser((User) entity);
        } else if (entity instanceof Review) {
            bookShop.addReview((Review) entity);
        }
    }

    private Book createBook(Map<String, Object> bookData) {
        String title = (String) bookData.get("title");
        Author author = bookShop.findAuthor((String) bookData.get("author"));
        String publisher = (String) bookData.get("publisher");
        int year = ((Number) bookData.get("year")).intValue();
        int price = ((Number) bookData.get("price")).intValue();
        String synopsis = (String) bookData.get("synopsis");
        String content = (String) bookData.get("content");
        List<String> genres = (List<String>) bookData.get("genres");

        Book book = new Book(title, author, publisher, year, price, synopsis, content, genres);
        
        // Find admin user by username and set it in Book object
        String username = (String) bookData.get("username");
        if (username != null) {
            User user = bookShop.findUser(username);
            if (user != null) {
                book.setAdmin(user);
            } else {
                System.out.println("User with username '" + username + "' not found for book: " + title);
            }
        }

        return book;
    }

    private Author createAuthor(Map<String, Object> authorData) {
        String name = (String) authorData.get("name");
        String penName = (String) authorData.get("penName");
        String nationality = (String) authorData.get("nationality");

        String bornStr = (String) authorData.get("born");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate born = LocalDate.parse(bornStr, formatter);

        String diedStr = (String) authorData.get("died");
        LocalDate died = diedStr == null ? null : LocalDate.parse(diedStr, formatter);

        Author author = new Author(name, penName, nationality, born, died);
        
        String username = (String) authorData.get("username");
        if (username != null) {
            User user = bookShop.findUser(username);
            if (user != null) {
                author.setUser(user);
            } else {
                System.out.println("User with username '" + username + "' not found for author: " + name);
            }
        }

        return author;
    }

    private User createUser(Map<String, Object> userData) {
        String username = (String) userData.get("username");
        String password = (String) userData.get("password");
        String email = (String) userData.get("email");
        Role role = Role.valueOf(userData.get("role").toString().toUpperCase());
        Map<String, String> addressData = (Map<String, String>) userData.get("address");
        Address address = new Address(addressData.get("country"), addressData.get("city"));

        return new User(username, password, email, role, address);
    }

    private Review createReview(Map<String, Object> reviewData) {
        String username = (String) reviewData.get("username");
        String title = (String) reviewData.get("title");
        int rate = ((Number) reviewData.get("rate")).intValue();
        String comment = (String) reviewData.get("comment");
        String dateStr = (String) reviewData.get("date");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);

        // Create Review object with initial values
        Review review = new Review(username, title, rate, comment, dateTime);
        
        // Find and set related user
        User user = bookShop.findUser(username);
        if (user != null) {
            review.setUser(user);
        } else {
            System.out.println("User with username '" + username + "' not found for review on book: " + title);
        }
        
        // Find and set related book
        Book book = bookShop.findBook(title);
        if (book != null) {
            review.setBook(book);
        } else {
            System.out.println("Book with title '" + title + "' not found for review by user: " + username);
        }

        return review;
    }
}