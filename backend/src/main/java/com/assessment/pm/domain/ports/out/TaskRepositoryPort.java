package com.assessment.pm.domain.ports.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.assessment.pm.domain.model.Task;

public interface TaskRepositoryPort {
    Task save(Task task);

    Optional<Task> findById(UUID id);

    List<Task> findByProjectId(UUID projectId);

    boolean existsById(UUID id);
}
