package com.example.pmapp.dto;

/**
 * Response DTO describing a material.  Used when sending material
 * details back to the client.  The id is included to allow
 * subsequent updates or deletions.
 */
public class MaterialResponse {
    private Integer id;
    private String name;
    private String marketId;

    public MaterialResponse() {}

    public MaterialResponse(Integer id, String name, String marketId) {
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
}