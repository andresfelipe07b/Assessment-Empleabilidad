package com.assessment.pm.infrastructure.adapter.output.persistence.repository;

import com.assessment.pm.infrastructure.adapter.output.persistence.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaTaskRepository extends JpaRepository<TaskEntity, UUID> {
    List<TaskEntity> findByProjectIdAndDeletedFalse(UUID projectId);
}
