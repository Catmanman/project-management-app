package com.example.pmapp.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for creating a new user account.  Only a
 * username and password are required; users are created with a
 * default role of USER.
 */
public class RegisterRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public RegisterRequest() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}