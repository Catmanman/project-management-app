package com.example.pmapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a project owned by a user.  Projects can have associated
 * materials (see {@link ProjectMaterial}).  The timestamps follow
 * typical project lifecycle fields such as creation date, estimated
 * completion and final completion.
 */
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "estimated_end")
    private LocalDateTime estimatedEnd;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    public Project() {}

    public Project(Integer id, User user, String name, String description, LocalDateTime createdAt,
                   LocalDateTime estimatedEnd, LocalDateTime finishedAt) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.estimatedEnd = estimatedEnd;
        this.finishedAt = finishedAt;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getEstimatedEnd() {
        return estimatedEnd;
    }

    public void setEstimatedEnd(LocalDateTime estimatedEnd) {
        this.estimatedEnd = estimatedEnd;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}