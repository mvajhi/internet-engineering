package org.example.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Authors")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "pen_name", length = 100)
    private String penName;
    
    @Column(name = "nationality", nullable = false, length = 50)
    private String nationality;
    
    @Column(name = "born", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate born;
    
    @Column(name = "died")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate died;
    
    @Column(name = "image_link")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String imageLink;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    public Author(String name, String penName, String nationality, LocalDate born, LocalDate died) {
        this.name = name;
        this.penName = penName;
        this.nationality = nationality;
        this.born = born;
        this.died = died;
    }

    public Author() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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