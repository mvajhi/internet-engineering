package org.example.request;

public class AddReviewRequest {
    int rate;
    String comment;
    String username;
    String title;

    public int getRate() {
        return rate;
    }

    public String getComment() {
        return comment;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public void setRate(int rate) {
        this.rate=rate;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTitle(String title) {
        this.title=title;
    }
}


