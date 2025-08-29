package com.example.pmapp.service;


import com.example.pmapp.MaterialMapper;
import com.example.pmapp.dto.MaterialRequest;
import com.example.pmapp.dto.MaterialResponse;
import com.example.pmapp.model.Material;
import com.example.pmapp.model.Role;
import com.example.pmapp.model.User;
import com.example.pmapp.repository.MaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MaterialService {
    private final MaterialRepository repo;
    public MaterialService(MaterialRepository repo) { this.repo = repo; }

    @Transactional(readOnly = true)
    public List<MaterialResponse> listMaterials() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    // MaterialService.java (snippet)
    @Transactional
    public MaterialResponse createMaterial(User caller, MaterialRequest request) {
        if (caller.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Only administrators can create materials");
        }
        Material m = new Material();
        m.setName(request.getName());
        m.setMarketId(request.getMarketId());
        m.setSeller(request.getSeller());
        // write into entity's `materialPicture`
        m.setMaterialPicture(request.getPictureUrl());

        m = repo.save(m);
        return MaterialMapper.toResponse(m);
    }

// MaterialService.java (snippet)




    @Transactional(readOnly = true)
    public List<MaterialResponse> listAll() {
        return repo.findAll()
                .stream()
                .map(MaterialMapper::toResponse)  // or just .map(toResponse) if you import static
                .toList();
    }


    @Transactional
    public void deleteMaterial(User caller, Long id) {
        if (caller.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Only administrators can delete materials");
        }
        repo.deleteById(id);
    }

    private MaterialResponse toResponse(Material m) {
        System.out.println("Material ID: " + m.getId() + ", Seller: " + m.getSeller() + ", Material Picture: " + m.getMaterialPicture());
        return new MaterialResponse(
                m.getId(), m.getName(), m.getMarketId(), m.getSeller(), m.getMaterialPicture()
        );
    }
}