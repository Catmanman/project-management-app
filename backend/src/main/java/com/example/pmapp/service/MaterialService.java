package com.example.pmapp.service;

import com.example.pmapp.dto.MaterialRequest;
import com.example.pmapp.dto.MaterialResponse;
import com.example.pmapp.model.Material;
import com.example.pmapp.model.Role;
import com.example.pmapp.model.User;
import com.example.pmapp.repository.MaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service encapsulating CRUD operations on materials.  Creation and
 * deletion are restricted to administrators.
 */
@Service
public class MaterialService {
    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    @Transactional(readOnly = true)
    public List<MaterialResponse> listMaterials() {
        return materialRepository.findAll().stream()
                .map(m -> new MaterialResponse(m.getId(), m.getName(), m.getMarketId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public MaterialResponse createMaterial(User caller, MaterialRequest request) {
        if (caller.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Only administrators can create materials");
        }
        Material material = new Material();
        material.setName(request.getName());
        material.setMarketId(request.getMarketId());
        material = materialRepository.save(material);
        return new MaterialResponse(material.getId(), material.getName(), material.getMarketId());
    }

    @Transactional
    public void deleteMaterial(User caller, Integer id) {
        if (caller.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Only administrators can delete materials");
        }
        materialRepository.deleteById(id);
    }
}