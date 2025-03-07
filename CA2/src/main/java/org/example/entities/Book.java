package org.example.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class Book {
    private String title;
    private Author author;
    private int year;
    private List<String> genres;
    private int price;
    private String synopsis;
    private String content;
    private String publisher;

}
