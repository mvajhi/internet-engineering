package org.example.entities;

import java.util.List;

public class Book {
    private String title;
    private Author author;
    private int year;
    private List<String> genres;
    private int price;
    private String synopsis;
    private String content;
    private String publisher;

    private float averageRating;
    private int reviewNumber;

    public Book(String title, Author author, String publisher, int year, int price, String synopsis, String content, List<String> genres) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.price = price;
        this.synopsis = synopsis;
        this.content = content;
        this.genres = genres;
    }

    public Book() {
    }

    public String getTitle() { return title; }
    public Author getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public int getYear() { return year; }
    public int getPrice() { return price; }
    public String getSynopsis() { return synopsis; }
    public String getContent() { return content; }
    public List<String> getGenres() { return genres; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public int getReviewNumber() {
        return reviewNumber;
    }

    public void setReviewNumber(int reviewNumber) {
        this.reviewNumber = reviewNumber;
    }
}
