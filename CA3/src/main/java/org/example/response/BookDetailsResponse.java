package org.example.response;

import org.example.entities.Author;
import org.example.entities.Book;

import java.util.List;

public class BookDetailsResponse {
    private String title;
    private Author author;
    private int year;
    private List<String> genres;
    private int price;
    private String synopsis;
    private int averageRating;
    private String publisher;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public int getContent() {
        return averageRating;
    }

    public void setContent(int content) {
        this.averageRating = content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public static BookDetailsResponse makeDetailedFromBook(Book book, int averageRating) {
        var b = new BookDetailsResponse();
        b.averageRating = averageRating;
        b.title = book.getTitle();
        b.author = book.getAuthor();
        b.year = book.getYear();
        b.genres = book.getGenres();
        b.price = book.getPrice();
        b.synopsis = book.getSynopsis();
        b.publisher = book.getPublisher();
        return b;
    }
}
