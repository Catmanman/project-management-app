// MaterialMapper.java
package com.example.pmapp;

import com.example.pmapp.dto.MaterialResponse;
import com.example.pmapp.model.Material;

public final class MaterialMapper {
    private MaterialMapper() {}

    public static MaterialResponse toResponse(Material m) {
        return new MaterialResponse(
                m.getId(),
                m.getName(),
                m.getMarketId(),
                m.getMaterialPicture(), // map DB column -> DTO pictureUrl
                m.getSeller()
        );
    }
}
