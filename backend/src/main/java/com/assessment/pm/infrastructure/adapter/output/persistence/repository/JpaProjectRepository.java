package com.assessment.pm.infrastructure.adapter.output.persistence.repository;

import com.assessment.pm.infrastructure.adapter.output.persistence.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaProjectRepository extends JpaRepository<ProjectEntity, UUID> {
    List<ProjectEntity> findByOwnerId(UUID ownerId);
}
