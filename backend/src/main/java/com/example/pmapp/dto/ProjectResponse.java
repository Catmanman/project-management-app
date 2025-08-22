package com.example.pmapp.dto;

/**
 * Response DTO for returning project details to the client.  Uses
 * basic string fields for timestamps to avoid leaking internal
 * `LocalDateTime` representation directly to the frontend.
 */
public class ProjectResponse {
    private Integer id;
    private String username;
    private String name;
    private String description;
    private String createdAt;
    private String estimatedEnd;
    private String finishedAt;

    public ProjectResponse() {}

    public ProjectResponse(Integer id, String username, String name, String description,
                           String createdAt, String estimatedEnd, String finishedAt) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.estimatedEnd = estimatedEnd;
        this.finishedAt = finishedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getEstimatedEnd() {
        return estimatedEnd;
    }

    public void setEstimatedEnd(String estimatedEnd) {
        this.estimatedEnd = estimatedEnd;
    }

    public String getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(String finishedAt) {
        this.finishedAt = finishedAt;
    }
}