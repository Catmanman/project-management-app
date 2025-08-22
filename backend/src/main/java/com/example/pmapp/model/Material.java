package com.example.pmapp.model;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * A material represents a resource that can be associated with a project.
 * The marketId field can be used to map to an external supplier or SKU.
 */
@Entity
@Table(name = "materials")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 120)
    private String name;

    @Column(name = "market_id", nullable = false, unique = true, length = 50)
    private String marketId;

    public Material() {}

    public Material(Integer id, String name, String marketId) {
        this.id = id;
        this.name = name;
        this.marketId = marketId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return Objects.equals(id, material.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}