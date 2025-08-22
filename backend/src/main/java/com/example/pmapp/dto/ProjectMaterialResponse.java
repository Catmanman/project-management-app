package com.example.pmapp.dto;

/**
 * Response describing a single project-material link.
 */
public class ProjectMaterialResponse {
    private Integer id;
    private Integer materialId;
    private String materialName;
    private String marketId;
    private double amount;

    public ProjectMaterialResponse() {}

    public ProjectMaterialResponse(Integer id, Integer materialId, String materialName, String marketId, double amount) {
        this.id = id;
        this.materialId = materialId;
        this.materialName = materialName;
        this.marketId = marketId;
        this.amount = amount;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getMaterialId() { return materialId; }
    public void setMaterialId(Integer materialId) { this.materialId = materialId; }

    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }

    public String getMarketId() { return marketId; }
    public void setMarketId(String marketId) { this.marketId = marketId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
