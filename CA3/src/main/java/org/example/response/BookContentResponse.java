package org.example.response;

import org.example.entities.Book;

import java.util.List;

public class BookContentResponse {
    String title;
    String content;

    public BookContentResponse(String title, String content) {
        this.title=title;
        this.content=content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content=content;
    }
}
