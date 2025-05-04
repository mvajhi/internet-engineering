package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title", nullable = false, length = 200)
    private String title;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
    
    @Column(name = "year")
    private Integer year;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "Book_Genres",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @JsonIgnore
    private Set<Genre> genres = new HashSet<>();
    
    @Transient
    private List<String> genreNames = new ArrayList<>();
    
    @Column(name = "price", precision = 10, scale = 2)
    private Integer price;
    
    @Column(name = "synopsis", columnDefinition = "TEXT")
    private String synopsis;
    
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "publisher", length = 100)
    private String publisher;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    @JsonIgnore
    private User admin;
    
    @Column(name = "image_link", columnDefinition = "TEXT")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String imageLink;
    
    @Transient
    private float averageRating;
    
    @Transient
    private int reviewNumber;

    public Book(String title, Author author, String publisher, int year, int price, String synopsis, String content, List<String> genres) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.price = price;
        this.synopsis = synopsis;
        this.content = content;
        this.genreNames = genres;
    }

    public Book() {
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getAdmin() {
        return admin;
    }
    
    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public String getTitle() { return title; }
    public Author getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public Integer getYear() { return year; }
    public Integer getPrice() { return price; }
    public String getSynopsis() { return synopsis; }
    public String getContent() { return content; }
    
    public List<String> getGenres() { 
        if (genreNames.isEmpty() && !genres.isEmpty()) {
            genres.forEach(genre -> genreNames.add(genre.getName()));
        }
        return genreNames;
    }
    
    public Set<Genre> getGenreEntities() {
        return genres;
    }
    
    public void setGenreEntities(Set<Genre> genres) {
        this.genres = genres;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setGenres(List<String> genres) {
        this.genreNames = genres;
    }

    public void setPrice(Integer price) {
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
    
    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
