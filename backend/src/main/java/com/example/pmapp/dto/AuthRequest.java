package com.example.pmapp.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for logging into the system.  Contains the
 * credentials provided by the client.
 */
public class AuthRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public AuthRequest() {}

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