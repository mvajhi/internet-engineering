package org.example.response;

import org.example.entities.Book;

import java.util.List;

public class CartResponses {
    String username;
    int totalCost;
    List<BookResponses> items;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username=username;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost=totalCost;
    }

    public List<BookResponses> getItems() {
        return items;
    }

    public void setItems(List<BookResponses> items) {
        this.items=items;
    }
}
