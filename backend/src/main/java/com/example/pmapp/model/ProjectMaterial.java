package com.example.pmapp.model;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * Association entity linking projects with the materials they require.
 * An amount specifies how much of a given material is needed for a
 * particular project.
 */
@Entity
@Table(name = "project_materials")
public class ProjectMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(optional = false)
    @JoinColumn(name = "material_id")
    private Material material;

    @Column(nullable = false)
    private double amount;

    public ProjectMaterial() {}

    public ProjectMaterial(Integer id, Project project, Material material, double amount) {
        this.id = id;
        this.project = project;
        this.material = material;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectMaterial that = (ProjectMaterial) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}