package com.assessment.pm.infrastructure.adapter.output.persistence.adapter;

import com.assessment.pm.domain.model.Task;
import com.assessment.pm.domain.ports.out.TaskRepositoryPort;
import com.assessment.pm.infrastructure.adapter.output.persistence.entity.TaskEntity;
import com.assessment.pm.infrastructure.adapter.output.persistence.repository.JpaTaskRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@SuppressWarnings("null")
public class TaskPersistenceAdapter implements TaskRepositoryPort {

    private final JpaTaskRepository jpaTaskRepository;

    public TaskPersistenceAdapter(JpaTaskRepository jpaTaskRepository) {
        this.jpaTaskRepository = jpaTaskRepository;
    }

    @Override
    public Task save(Task task) {
        TaskEntity entity = toEntity(task);
        TaskEntity saved = jpaTaskRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return jpaTaskRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Task> findByProjectId(UUID projectId) {
        return jpaTaskRepository.findByProjectId(projectId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaTaskRepository.existsById(id);
    }

    private TaskEntity toEntity(Task task) {
        return new TaskEntity(
                task.getId(),
                task.getProjectId(),
                task.getTitle(),
                task.isCompleted(),
                task.isDeleted());
    }

    private Task toDomain(TaskEntity entity) {
        return new Task(
                entity.getId(),
                entity.getProjectId(),
                entity.getTitle(),
                entity.isCompleted(),
                entity.isDeleted());
    }
}
