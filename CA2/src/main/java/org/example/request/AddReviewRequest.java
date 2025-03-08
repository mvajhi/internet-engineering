package org.example.request;

public class AddReviewRequest {
    int rating;
    String comment;
    String username;
    String bookTitle;

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getUsername() {
        return username;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
}


