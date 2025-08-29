// MaterialRequest.java
package com.example.pmapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MaterialRequest {
    @NotBlank
    @Size(max = 120)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String marketId;

    @Size(max = 120)
    private String seller;

    @Size(max = 255)
    private String pictureUrl;

    public MaterialRequest() {}

    public MaterialRequest(String name, String marketId, String seller, String pictureUrl) {
        this.name = name;
        this.marketId = marketId;
        this.seller = seller;
        this.pictureUrl = pictureUrl;
    }

    public String getName() { return name; }
    public String getMarketId() { return marketId; }
    public String getSeller() { return seller; }
    public String getPictureUrl() { return pictureUrl; }

    public void setName(String name) { this.name = name; }
    public void setMarketId(String marketId) { this.marketId = marketId; }
    public void setSeller(String seller) { this.seller = seller; }
    public void setPictureUrl(String pictureUrl) { this.pictureUrl = pictureUrl; }
}
