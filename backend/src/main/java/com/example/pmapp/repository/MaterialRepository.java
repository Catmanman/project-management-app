package com.example.pmapp.repository;

import com.example.pmapp.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for material entities.
 */
public interface MaterialRepository extends JpaRepository<Material, Integer> {
}