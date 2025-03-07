package org.example.entities;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Review {
    int rating;
    String comment;
    Date date;
    User user;
    Book book;
}
