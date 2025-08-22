package com.example.pmapp.controller;

import com.example.pmapp.dto.ProjectMaterialRequest;
import com.example.pmapp.dto.ProjectMaterialResponse;
import com.example.pmapp.model.User;
import com.example.pmapp.repository.UserRepository;
import com.example.pmapp.service.ProjectMaterialService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/materials")
public class ProjectMaterialController {

    private final ProjectMaterialService service;
    private final UserRepository userRepository;

    public ProjectMaterialController(ProjectMaterialService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProjectMaterialResponse>> list(@AuthenticationPrincipal UserDetails userDetails,
                                                              @PathVariable Integer projectId) {
        User caller = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(service.list(caller, projectId));
    }

    @PostMapping
    public ResponseEntity<ProjectMaterialResponse> upsert(@AuthenticationPrincipal UserDetails userDetails,
                                                          @PathVariable Integer projectId,
                                                          @Valid @RequestBody ProjectMaterialRequest req) {
        User caller = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        ProjectMaterialResponse resp = service.upsert(caller, projectId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails userDetails,
                                       @PathVariable Integer projectId,
                                       @PathVariable Integer id) {
        User caller = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        service.delete(caller, projectId, id);
        return ResponseEntity.noContent().build();
    }
}
