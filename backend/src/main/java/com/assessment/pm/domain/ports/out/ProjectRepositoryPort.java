package com.assessment.pm.domain.ports.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.assessment.pm.domain.model.Project;

public interface ProjectRepositoryPort {
    Project save(Project project);

    Optional<Project> findById(UUID id);

    List<Project> findAll(); // Or findAllByOwnerId

    List<Project> findByOwnerId(UUID ownerId);

    boolean existsById(UUID id);
}
