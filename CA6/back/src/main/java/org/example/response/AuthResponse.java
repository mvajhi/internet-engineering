package org.example.response;

import java.util.Map;

public class AuthResponse extends Response {
    private String token;
    private String username;

    public AuthResponse(boolean status, String message, String token, String username) {
        super(status, message, null);
        this.token = token;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}