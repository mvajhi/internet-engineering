package org.example.response;

import org.example.entities.Book;
import java.util.List;
import java.util.ArrayList;

public class AuthorBooksResponse {
    private String authorName;
    private List<BookSummary> books = new ArrayList<>();
    
    public AuthorBooksResponse() {}
    
    public AuthorBooksResponse(String authorName, List<Book> books) {
        this.authorName = authorName;
        if (books != null) {
            for (Book book : books) {
                this.books.add(new BookSummary(book));
            }
        }
    }
    
    public String getAuthorName() {
        return authorName;
    }
    
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    
    public List<BookSummary> getBooks() {
        return books;
    }
    
    public void setBooks(List<BookSummary> books) {
        this.books = books;
    }
    
    // Inner class to represent simplified book data
    public static class BookSummary {
        private String title;
        private Integer year;
        private List<String> genres;
        private Integer price;
        private String synopsis;
        private String publisher;
        private String imageLink;
        private float averageRating;
        
        public BookSummary(Book book) {
            this.title = book.getTitle();
            this.year = book.getYear();
            this.genres = book.getGenres();
            this.price = book.getPrice();
            this.synopsis = book.getSynopsis();
            this.publisher = book.getPublisher();
            this.imageLink = book.getImageLink();
            this.averageRating = book.getAverageRating();
        }
        
        public String getTitle() {
            return title;
        }
        
        public Integer getYear() {
            return year;
        }
        
        public List<String> getGenres() {
            return genres;
        }
        
        public Integer getPrice() {
            return price;
        }
        
        public String getSynopsis() {
            return synopsis;
        }
        
        public String getPublisher() {
            return publisher;
        }
        
        public String getImageLink() {
            return imageLink;
        }
        
        public float getAverageRating() {
            return averageRating;
        }
    }
}