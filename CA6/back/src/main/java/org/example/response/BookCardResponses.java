package org.example.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

public class BookCardResponses {
    private String title;
    private String author;
    private int price;
    private float averageRating;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String imageLink;

    public BookCardResponses() {
    }

    public BookCardResponses(String imageLink, float averageRating, int price, String author, String title) {
        this.imageLink=imageLink;
        this.averageRating=averageRating;
        this.price=price;
        this.author=author;
        this.title=title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author=author;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price=price;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating=averageRating;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink=imageLink;
    }
}
