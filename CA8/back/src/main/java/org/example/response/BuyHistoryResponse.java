package org.example.response;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class BuyHistoryResponse {
    String username;
    List<PurchaseHistoryResponse> purchasesHistory;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username=username;
    }

    public List<PurchaseHistoryResponse> getPurchasesHistory() {
        return purchasesHistory;
    }

    public void setPurchasesHistory(List<PurchaseHistoryResponse> purchasesHistory) {
        this.purchasesHistory=purchasesHistory;
    }
}
