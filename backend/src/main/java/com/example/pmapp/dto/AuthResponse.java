package com.example.pmapp.dto;

/**
 * Response returned after a successful authentication.  Contains the
 * JWT token used for authorising subsequent requests and basic user
 * profile information.
 */
public class AuthResponse {
    private String username;
    private int id;
    private String role;
    private String token;

    public AuthResponse() {}

    public AuthResponse(String username, int id, String role, String token) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}