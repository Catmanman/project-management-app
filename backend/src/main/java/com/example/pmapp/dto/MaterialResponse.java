 package com.example.pmapp.dto;

/**
 * Response DTO describing a material.  Used when sending material
 * details back to the client.  The id is included to allow
 * subsequent updates or deletions.
 */
public class MaterialResponse {
    private Long id;
    private String name;
    private String marketId;
    private String seller;
    private String pictureUrl;
    public MaterialResponse() {}

    public MaterialResponse(Long id, String name, String marketId, String pictureUrl, String seller) {
        this.id = id;
        this.name = name;
        this.marketId = marketId;
        this.seller = seller;
        this.pictureUrl = pictureUrl;
    }

    public String getPictureUrl() {return pictureUrl;}

    public void setPictureUrl(String pictureUrl) {this.pictureUrl = pictureUrl;}

    public String getSeller() {return seller;}

    public void setSeller(String seller) {this.seller = seller;}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
