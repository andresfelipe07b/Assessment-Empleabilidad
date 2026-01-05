package com.assessment.pm.application.service;

import com.assessment.pm.domain.model.Project;
import com.assessment.pm.domain.model.Task;
import com.assessment.pm.domain.ports.in.ActivateProjectUseCase;
import com.assessment.pm.domain.ports.in.CreateProjectUseCase;
import com.assessment.pm.domain.ports.in.ListProjectsUseCase;
import com.assessment.pm.domain.ports.out.AuditLogPort;
import com.assessment.pm.domain.ports.out.NotificationPort;
import com.assessment.pm.domain.ports.out.ProjectRepositoryPort;
import com.assessment.pm.domain.ports.out.TaskRepositoryPort;

import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ProjectService implements CreateProjectUseCase, ActivateProjectUseCase, ListProjectsUseCase {

    private final ProjectRepositoryPort projectRepository;
    private final TaskRepositoryPort taskRepository;
    private final NotificationPort notificationPort;
    private final AuditLogPort auditLogPort;

    public ProjectService(ProjectRepositoryPort projectRepository,
            TaskRepositoryPort taskRepository,
            NotificationPort notificationPort,
            AuditLogPort auditLogPort) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.notificationPort = notificationPort;
        this.auditLogPort = auditLogPort;
    }

    @Override
    public Project createProject(String name, UUID ownerId) {
        Project project = new Project(UUID.randomUUID(), ownerId, name);
        return projectRepository.save(project);
    }

    @Override
    public Project activateProject(UUID projectId, UUID ownerId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        if (!project.isOwner(ownerId)) {
            throw new SecurityException("User is not the owner of this project");
        }

        List<Task> tasks = taskRepository.findByProjectId(projectId);
        project.activate(tasks);
        Project saved = projectRepository.save(project);

        auditLogPort.register("PROJECT_ACTIVATED", projectId);
        notificationPort.notify("Project " + projectId + " activated.");

        return saved;
    }

    @Override
    public List<Project> listProjects(UUID ownerId) {
        return projectRepository.findByOwnerId(ownerId);
    }

    @Override
    public Project getProject(UUID projectId, UUID ownerId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        if (!project.isOwner(ownerId)) {
            throw new SecurityException("User is not the owner of this project");
        }
        return project;
    }
}
