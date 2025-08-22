package com.example.pmapp.service;

import com.example.pmapp.dto.ProjectRequest;
import com.example.pmapp.dto.ProjectResponse;
import com.example.pmapp.model.Project;
import com.example.pmapp.model.Role;
import com.example.pmapp.model.User;
import com.example.pmapp.repository.ProjectRepository;
import com.example.pmapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service encapsulating core project operations.  Handles conversion
 * between request/response DTOs and JPA entities and enforces
 * ownership/role based rules.
 */
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieve a list of projects for the given user.  Admins can
     * retrieve all projects, whereas regular users will only see
     * projects they own.
     */
    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjectsFor(User user) {
        List<Project> projects;
        if (user.getRole() == Role.ADMIN) {
            projects = projectRepository.findAll();
        } else {
            projects = projectRepository.findByUser(user);
        }
        return projects.stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     * Create a new project owned by the provided user.  Ignores the
     * userId on the request payload to prevent forging projects for
     * other users.
     */
    @Transactional
    public ProjectResponse createProject(User user, ProjectRequest request) {
        Project project = new Project();
        project.setUser(user);
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        // Parse optional dates
        if (request.getEstimatedEnd() != null && !request.getEstimatedEnd().isBlank()) {
            try {
                project.setEstimatedEnd(LocalDateTime.parse(request.getEstimatedEnd()));
            } catch (DateTimeParseException ignore) {}
        }
        if (request.getFinishedAt() != null && !request.getFinishedAt().isBlank()) {
            try {
                project.setFinishedAt(LocalDateTime.parse(request.getFinishedAt()));
            } catch (DateTimeParseException ignore) {}
        }
        project = projectRepository.save(project);
        return toResponse(project);
    }

    /**
     * Delete a project.  Users may delete their own projects; admins may
     * delete any project.  If the project does not exist or the
     * caller is not authorised, an IllegalArgumentException is thrown.
     */
    @Transactional
    public void deleteProject(User caller, Integer id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        if (!caller.getRole().equals(Role.ADMIN) && !project.getUser().getId().equals(caller.getId())) {
            throw new IllegalArgumentException("Forbidden");
        }
        projectRepository.delete(project);
    }

    private ProjectResponse toResponse(Project project) {
        String createdAt = project.getCreatedAt() != null ? project.getCreatedAt().toString() : null;
        String estimatedEnd = project.getEstimatedEnd() != null ? project.getEstimatedEnd().toString() : null;
        String finishedAt = project.getFinishedAt() != null ? project.getFinishedAt().toString() : null;
        return new ProjectResponse(
                project.getId(),
                project.getUser().getUsername(),
                project.getName(),
                project.getDescription(),
                createdAt,
                estimatedEnd,
                finishedAt
        );
    }

    /**
     * Update an existing project.  Only the owner of the project or an
     * administrator may perform this operation.  Fields that are null or
     * blank in the request will result in the corresponding property being
     * cleared on the project.  Dates are parsed from ISO‑8601 strings.
     */
    @Transactional
    public ProjectResponse updateProject(User caller, Integer id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        // enforce ownership/admin access
        if (!caller.getRole().equals(Role.ADMIN) && !project.getUser().getId().equals(caller.getId())) {
            throw new IllegalArgumentException("Forbidden");
        }
        // update name/description if provided (optional)
        if (request.getName() != null && !request.getName().isBlank()) {
            project.setName(request.getName());
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            project.setDescription(request.getDescription());
        }
        // update estimated end: parse or clear
        if (request.getEstimatedEnd() != null && !request.getEstimatedEnd().isBlank()) {
            try {
                project.setEstimatedEnd(LocalDateTime.parse(request.getEstimatedEnd()));
            } catch (DateTimeParseException ignore) {
                project.setEstimatedEnd(null);
            }
        } else {
            project.setEstimatedEnd(null);
        }
        // update finishedAt: parse or clear
        if (request.getFinishedAt() != null && !request.getFinishedAt().isBlank()) {
            try {
                project.setFinishedAt(LocalDateTime.parse(request.getFinishedAt()));
            } catch (DateTimeParseException ignore) {
                project.setFinishedAt(null);
            }
        } else {
            project.setFinishedAt(null);
        }
        project = projectRepository.save(project);
        return toResponse(project);
    }
}