package com.dacs.bookwise.dto;

public class AuthResponse {

    private String message;
    private String token;

    // Constructor vacío
    public AuthResponse() {}

    // Constructor con parámetros
    public AuthResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    // Getters y Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
