package com.example.pmapp.repository;

import com.example.pmapp.model.Project;
import com.example.pmapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for project entities.  Provides methods to query
 * projects by their owning user.
 */
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByUser(User user);
    List<Project> findByUserId(Integer userId);
}