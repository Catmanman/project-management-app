package com.example.pmapp.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for creating or updating a material.  Both the name and
 * market identifier must be provided and will be validated by the
 * controller.
 */
public class MaterialRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String marketId;

    public MaterialRequest() {}

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
}