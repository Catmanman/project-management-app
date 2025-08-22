package com.example.pmapp.service;

import com.example.pmapp.dto.ProjectMaterialRequest;
import com.example.pmapp.dto.ProjectMaterialResponse;
import com.example.pmapp.model.*;
import com.example.pmapp.repository.MaterialRepository;
import com.example.pmapp.repository.ProjectMaterialRepository;
import com.example.pmapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectMaterialService {

    private final ProjectRepository projectRepository;
    private final MaterialRepository materialRepository;
    private final ProjectMaterialRepository projectMaterialRepository;

    public ProjectMaterialService(ProjectRepository projectRepository,
                                  MaterialRepository materialRepository,
                                  ProjectMaterialRepository projectMaterialRepository) {
        this.projectRepository = projectRepository;
        this.materialRepository = materialRepository;
        this.projectMaterialRepository = projectMaterialRepository;
    }

    @Transactional(readOnly = true)
    public List<ProjectMaterialResponse> list(User caller, Integer projectId) {
        Project project = getAndAuthorize(caller, projectId);
        return projectMaterialRepository.findByProject(project)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProjectMaterialResponse upsert(User caller, Integer projectId, ProjectMaterialRequest req) {
        if (req.getMaterialId() == null) throw new IllegalArgumentException("materialId required");
        if (req.getAmount() < 0) throw new IllegalArgumentException("amount must be >= 0");

        Project project = getAndAuthorize(caller, projectId);
        Material material = materialRepository.findById(req.getMaterialId())
                .orElseThrow(() -> new IllegalArgumentException("Material not found"));

        ProjectMaterial pm = projectMaterialRepository
                .findByProjectAndMaterial(project, material)
                .orElseGet(() -> {
                    ProjectMaterial n = new ProjectMaterial();
                    n.setProject(project);
                    n.setMaterial(material);
                    return n;
                });

        pm.setAmount(req.getAmount());
        pm = projectMaterialRepository.save(pm);
        return toResponse(pm);
    }

    @Transactional
    public void delete(User caller, Integer projectId, Integer projectMaterialId) {
        ProjectMaterial pm = projectMaterialRepository.findById(projectMaterialId)
                .orElseThrow(() -> new IllegalArgumentException("Project material not found"));

        // Ensure the path projectId matches the entity and caller has rights
        if (!pm.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException("Mismatched project id");
        }
        authorize(caller, pm.getProject());
        projectMaterialRepository.delete(pm);
    }

    /* helpers */

    private Project getAndAuthorize(User caller, Integer projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        authorize(caller, project);
        return project;
    }

    private void authorize(User caller, Project project) {
        boolean admin = caller.getRole() == Role.ADMIN;
        boolean owner = project.getUser() != null && project.getUser().getId().equals(caller.getId());
        if (!(admin || owner)) {
            throw new IllegalArgumentException("Forbidden");
        }
    }

    private ProjectMaterialResponse toResponse(ProjectMaterial pm) {
        return new ProjectMaterialResponse(
                pm.getId(),
                pm.getMaterial().getId(),
                pm.getMaterial().getName(),
                pm.getMaterial().getMarketId(),
                pm.getAmount()
        );
    }
}
