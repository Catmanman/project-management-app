package com.example.pmapp.controller;

import com.example.pmapp.dto.MaterialRequest;
import com.example.pmapp.dto.MaterialResponse;
import com.example.pmapp.model.User;
import com.example.pmapp.service.MaterialService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {
    private final MaterialService svc;
    public MaterialController(MaterialService svc) { this.svc = svc; }

    @GetMapping
    public List<MaterialResponse> list() {
        return svc.listMaterials();
    }

    @PostMapping
    public MaterialResponse create(@AuthenticationPrincipal User caller,
                                   @RequestBody MaterialRequest req) {
        return svc.createMaterial(caller, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@AuthenticationPrincipal User caller, @PathVariable Long id) {
        svc.deleteMaterial(caller, id);
    }
}
