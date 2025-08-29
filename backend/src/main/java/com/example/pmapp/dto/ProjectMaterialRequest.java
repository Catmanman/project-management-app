package com.example.pmapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Request to associate a material to a project (or update its amount).
 */
public class ProjectMaterialRequest {
    @NotNull
    private Long materialId;

    @Min(0)
    private double amount;

    public ProjectMaterialRequest() {}

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
