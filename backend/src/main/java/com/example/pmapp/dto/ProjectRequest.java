package com.example.pmapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data transfer object used when creating or updating a project.
 * Dates are represented as ISO-8601 strings to minimise client-side
 * parsing issues.  Null values are permitted for estimatedEnd
 * and finishedAt when those fields are not known.
 */
public class ProjectRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    // The owning user's id.  This field is required on create, but
    // ignored on update.  On the server we derive the authenticated
    // user from the security context and ignore this property to
    // prevent privilege escalation.

    private String username ;

    private String estimatedEnd; // ISO date-time string or null

    private String finishedAt;   // ISO date-time string or null

    public ProjectRequest() {}

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