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

    public String getTitle() { return title; }
    public Author getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public int getYear() { return year; }
    public int getPrice() { return price; }
    public String getSynopsis() { return synopsis; }
    public String getContent() { return content; }
    public List<String> getGenres() { return genres; }

}
