package com.example.pmapp.repository;

import com.example.pmapp.model.Material;
import com.example.pmapp.model.Project;
import com.example.pmapp.model.ProjectMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for the association between projects and materials.
 */
public interface ProjectMaterialRepository extends JpaRepository<ProjectMaterial, Integer> {
    List<ProjectMaterial> findByProject(Project project);
    void deleteByProject(Project project);

    Optional<ProjectMaterial> findByProjectAndMaterial(Project project, Material material);
}
