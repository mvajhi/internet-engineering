package org.example.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.example.entities.Author;
import org.example.entities.Book;

import java.util.List;

public class BookDetailsResponse {
    private String title;
    private String author;
    private int year;
    private List<String> genres;
    private int price;
    private String synopsis;
    private float averageRating;
    private String publisher;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String imageLink;

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating=averageRating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
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


    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public static BookDetailsResponse makeDetailedFromBook(Book book, float averageRating) {
        var b = new BookDetailsResponse();
        b.averageRating = averageRating;
        b.title = book.getTitle();
        b.author = book.getAuthor().getName();
        b.year = book.getYear();
        b.genres = book.getGenres();
        b.price = book.getPrice();
        b.synopsis = book.getSynopsis();
        b.publisher = book.getPublisher();
        b.imageLink = book.getImageLink();
        return b;
    }
}
