package org.example.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

public class BookResponses {
    String title;
    String author;
    String publisher;
    List<String> genres;
    int year;
    int price;
    boolean isBorrowed;
    int finalPrice;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    int borrowedDays;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime borrowedDate;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    Integer totalBuy = -1;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String imageLink;

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public BookResponses(String title, String author, String publisher, List<String> genres, int year, int price, boolean isBorrowed, int finalPrice, int borrowedDays) {
        this.title=title;
        this.author=author;
        this.publisher=publisher;
        this.genres=genres;
        this.year=year;
        this.price=price;
        this.isBorrowed=isBorrowed;
        this.finalPrice=finalPrice;
        this.borrowedDays=borrowedDays;
    }

    public BookResponses(String title, String author, String publisher, List<String> genres, int year, int price) {
        this.title=title;
        this.author=author;
        this.publisher=publisher;
        this.genres=genres;
        this.year=year;
        this.price=price;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher=publisher;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres=genres;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year=year;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price=price;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed=borrowed;
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(int finalPrice) {
        this.finalPrice=finalPrice;
    }

    public int getBorrowedDays() {
        return borrowedDays;
    }

    public void setBorrowedDays(int borrowedDays) {
        this.borrowedDays=borrowedDays;
    }

    public LocalDateTime getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(LocalDateTime borrowedDate) {
        this.borrowedDate=borrowedDate;
    }

    public Integer getTotalBuy() {
        return totalBuy;
    }

    public void setTotalBuy(Integer totalBuy) {
        this.totalBuy=totalBuy;
    }
}
