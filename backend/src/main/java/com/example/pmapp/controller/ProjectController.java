package com.example.pmapp.controller;

import com.example.pmapp.dto.ProjectRequest;
import com.example.pmapp.dto.ProjectResponse;
import com.example.pmapp.model.User;
import com.example.pmapp.repository.UserRepository;
import com.example.pmapp.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller exposing CRUD operations for projects.  All endpoints
 * require authentication.  Users may only operate on their own
 * projects unless they have the ADMIN role.
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final UserRepository userRepository;

    public ProjectController(ProjectService projectService, UserRepository userRepository) {
        this.projectService = projectService;
        this.userRepository = userRepository;
    }

    /**
     * List projects for the current user.  Admins will receive all
     * projects.
     */
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> listProjects(@AuthenticationPrincipal UserDetails userDetails) {
        User caller = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        List<ProjectResponse> projects = projectService.getProjectsFor(caller);
        return ResponseEntity.ok(projects);
    }

    /**
     * Create a new project.  The authenticated user becomes the owner
     * regardless of the userId specified in the request payload.
     */
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@AuthenticationPrincipal UserDetails userDetails,
                                                         @Valid @RequestBody ProjectRequest request) {
        User caller = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        ProjectResponse response = projectService.createProject(caller, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Delete a project by id.  Only the owner or an admin may perform
     * this operation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@AuthenticationPrincipal UserDetails userDetails,
                                              @PathVariable Integer id) {
        User caller = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        try {
            projectService.deleteProject(caller, id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Update an existing project.  Only the owner or an admin may update
     * a project.  Fields left blank or null in the request will be
     * cleared on the entity.  The username on the request is ignored.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@AuthenticationPrincipal UserDetails userDetails,
                                                         @PathVariable Integer id,
                                                         @Valid @RequestBody ProjectRequest request) {
        User caller = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        try {
            ProjectResponse updated = projectService.updateProject(caller, id, request);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}