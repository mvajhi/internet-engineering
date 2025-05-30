package org.example.response;


import java.util.List;

public class UserBookResponse {
    String username;
    List<BookResponses> books;

    public UserBookResponse() {
    }

    public UserBookResponse(String username, List<BookResponses> books) {
        this.username=username;
        this.books=books;
    }

    public List<BookResponses> getBooks() {
        return books;
    }

    public void setBooks(List<BookResponses> books) {
        this.books=books;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username=username;
    }

}
