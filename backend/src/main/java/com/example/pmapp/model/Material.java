package com.example.pmapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "materials")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                       // bigint -> Long

    @Column(nullable = false, unique = true, length = 120)
    private String name;

    @Column(name = "market_id", nullable = false, unique = true, length = 50)
    private String marketId;

    @Column(name = "seller", nullable=false, length = 120)
    private String seller;

    @Column(name = "material_picture", length = 255)
    @JsonProperty("pictureUrl")            // frontend key = pictureUrl
    private String materialPicture;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMarketId() { return marketId; }
    public void setMarketId(String marketId) { this.marketId = marketId; }
    public String getSeller() { return seller; }
    public void setSeller(String seller) { this.seller = seller; }
    public String getMaterialPicture() { return materialPicture; }
    public void setMaterialPicture(String materialPicture) { this.materialPicture = materialPicture; }
}
