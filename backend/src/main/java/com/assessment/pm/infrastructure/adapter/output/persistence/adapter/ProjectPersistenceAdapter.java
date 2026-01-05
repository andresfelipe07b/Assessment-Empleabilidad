package com.assessment.pm.infrastructure.adapter.output.persistence.adapter;

import com.assessment.pm.domain.model.Project;
import com.assessment.pm.domain.model.ProjectStatus;
import com.assessment.pm.domain.ports.out.ProjectRepositoryPort;
import com.assessment.pm.infrastructure.adapter.output.persistence.entity.ProjectEntity;
import com.assessment.pm.infrastructure.adapter.output.persistence.repository.JpaProjectRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProjectPersistenceAdapter implements ProjectRepositoryPort {

    private final JpaProjectRepository jpaProjectRepository;

    public ProjectPersistenceAdapter(JpaProjectRepository jpaProjectRepository) {
        this.jpaProjectRepository = jpaProjectRepository;
    }

    @Override
    public Project save(Project project) {
        ProjectEntity entity = toEntity(project);
        ProjectEntity saved = jpaProjectRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Project> findById(UUID id) {
        return jpaProjectRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Project> findAll() {
        return jpaProjectRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Project> findByOwnerId(UUID ownerId) {
        return jpaProjectRepository.findByOwnerId(ownerId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaProjectRepository.existsById(id);
    }

    private ProjectEntity toEntity(Project project) {
        return new ProjectEntity(
                project.getId(),
                project.getOwnerId(),
                project.getName(),
                project.getStatus().name(),
                project.isDeleted());
    }

    private Project toDomain(ProjectEntity entity) {
        return new Project(
                entity.getId(),
                entity.getOwnerId(),
                entity.getName(),
                convertStatus(entity.getStatus()),
                entity.isDeleted());
    }

    private ProjectStatus convertStatus(String status) {
        if (status == null)
            return ProjectStatus.DRAFT;
        try {
            return ProjectStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            return ProjectStatus.DRAFT; // Fallback for invalid data
        }
    }
}
