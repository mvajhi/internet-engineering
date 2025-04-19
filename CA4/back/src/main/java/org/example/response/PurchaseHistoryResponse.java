package org.example.response;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class PurchaseHistoryResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime purchaseDate;
    int totalCost;
    List<BookResponses> items;

    public PurchaseHistoryResponse(LocalDateTime purchaseDate, int totalCost, List<BookResponses> items) {
        this.purchaseDate=purchaseDate;
        this.totalCost=totalCost;
        this.items=items;
    }

    public PurchaseHistoryResponse() {
    }

    public List<BookResponses> getItems() {
        return items;
    }

    public void setItems(List<BookResponses> items) {
        this.items=items;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate=purchaseDate;
    }
}
