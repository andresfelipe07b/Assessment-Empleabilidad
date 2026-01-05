package com.assessment.pm.application.service;

import com.assessment.pm.domain.model.Project;
import com.assessment.pm.domain.model.Task;
import com.assessment.pm.domain.ports.in.CompleteTaskUseCase;
import com.assessment.pm.domain.ports.in.CreateTaskUseCase;
import com.assessment.pm.domain.ports.in.ListTasksUseCase;
import com.assessment.pm.domain.ports.in.DeleteTaskUseCase;
import com.assessment.pm.domain.ports.out.AuditLogPort;
import com.assessment.pm.domain.ports.out.NotificationPort;
import com.assessment.pm.domain.ports.out.ProjectRepositoryPort;
import com.assessment.pm.domain.ports.out.TaskRepositoryPort;

import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public class TaskService implements CreateTaskUseCase, CompleteTaskUseCase, ListTasksUseCase, DeleteTaskUseCase {

    private final TaskRepositoryPort taskRepository;
    private final ProjectRepositoryPort projectRepository;
    private final NotificationPort notificationPort;
    private final AuditLogPort auditLogPort;

    public TaskService(TaskRepositoryPort taskRepository,
            ProjectRepositoryPort projectRepository,
            NotificationPort notificationPort,
            AuditLogPort auditLogPort) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.notificationPort = notificationPort;
        this.auditLogPort = auditLogPort;
    }

    @Override
    public Task createTask(String title, UUID projectId, UUID ownerId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        if (!project.isOwner(ownerId)) {
            throw new SecurityException("User is not the owner of this project");
        }

        Task task = new Task(UUID.randomUUID(), projectId, title);
        return taskRepository.save(task);
    }

    @Override
    public Task completeTask(UUID taskId, UUID ownerId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        Project project = projectRepository.findById(task.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found associated with task"));

        if (!project.isOwner(ownerId)) {
            throw new SecurityException("User is not the owner of this task's project");
        }

        task.complete();
        Task saved = taskRepository.save(task);

        auditLogPort.register("TASK_COMPLETED", taskId);
        notificationPort.notify("Task " + taskId + " completed.");

        return saved;
    }

    @Override
    public List<Task> listTasks(UUID projectId, UUID ownerId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        if (!project.isOwner(ownerId)) {
            throw new SecurityException("User is not the owner of this project");
        }

        return taskRepository.findByProjectId(projectId);
    }

    @Override
    public void deleteTask(UUID taskId, UUID ownerId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        Project project = projectRepository.findById(task.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found associated with task"));

        if (!project.isOwner(ownerId)) {
            throw new SecurityException("User is not the owner of this task's project");
        }

        task.delete(); // Need to add this method to Task domain
        taskRepository.save(task);

        auditLogPort.register("TASK_DELETED", taskId);
    }
}
