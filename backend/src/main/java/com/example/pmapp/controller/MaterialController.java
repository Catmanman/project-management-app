package com.example.pmapp.controller;

import com.example.pmapp.dto.MaterialRequest;
import com.example.pmapp.dto.MaterialResponse;
import com.example.pmapp.model.User;
import com.example.pmapp.repository.UserRepository;
import com.example.pmapp.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller handling CRUD endpoints for materials.  Listing is
 * available to all authenticated users.  Creation and deletion are
 * restricted to ADMINs.
 */
@RestController
@RequestMapping("/api/materials")
public class MaterialController {
    private final MaterialService materialService;
    private final UserRepository userRepository;

    public MaterialController(MaterialService materialService, UserRepository userRepository) {
        this.materialService = materialService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<MaterialResponse>> list() {
        return ResponseEntity.ok(materialService.listMaterials());
    }

    @PostMapping
    public ResponseEntity<MaterialResponse> create(@AuthenticationPrincipal UserDetails userDetails,
                                                   @Valid @RequestBody MaterialRequest request) {
        User caller = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        try {
            MaterialResponse response = materialService.createMaterial(caller, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer id) {
        User caller = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        try {
            materialService.deleteMaterial(caller, id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}