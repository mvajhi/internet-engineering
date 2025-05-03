package org.example.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Author {
    private String name;
    private String penName;
    private String nationality;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate born;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate died;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String imageLink;

    public Author(String name, String penName, String nationality, LocalDate born, LocalDate died) {
        this.name = name;
        this.penName = penName;
        this.nationality = nationality;
        this.born = born;
        this.died = died;
    }

    public Author() {}

    public String getName() { return name; }
    public String getPenName() { return penName; }
    public String getNationality() { return nationality; }
    public LocalDate getBorn() { return born; }
    public LocalDate getDied() { return died; }

    public void setName(String name) {
        this.name = name;
    }

    public void setPenName(String penName) {
        this.penName = penName;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setBorn(LocalDate born) {
        this.born = born;
    }

    public void setDied(LocalDate died) {
        this.died = died;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}