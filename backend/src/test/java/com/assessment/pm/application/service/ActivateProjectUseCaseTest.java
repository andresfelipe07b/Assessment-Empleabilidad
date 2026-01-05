package com.assessment.pm.application.service;

import com.assessment.pm.domain.model.Project;
import com.assessment.pm.domain.model.ProjectStatus;
import com.assessment.pm.domain.model.Task;
import com.assessment.pm.domain.ports.out.AuditLogPort;
import com.assessment.pm.domain.ports.out.NotificationPort;
import com.assessment.pm.domain.ports.out.ProjectRepositoryPort;
import com.assessment.pm.domain.ports.out.TaskRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivateProjectUseCaseTest {

    @Mock
    private ProjectRepositoryPort projectRepository;
    @Mock
    private TaskRepositoryPort taskRepository;
    @Mock
    private NotificationPort notificationPort;
    @Mock
    private AuditLogPort auditLogPort;

    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectService = new ProjectService(projectRepository, taskRepository, notificationPort, auditLogPort);
    }

    @Test
    void ActivateProject_WithTasks_ShouldSucceed() {
        UUID ownerId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();
        Project project = new Project(projectId, ownerId, "Test Project");
        Task task = new Task(UUID.randomUUID(), projectId, "Task 1");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.findByProjectId(projectId)).thenReturn(List.of(task));
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArguments()[0]);

        Project activatedProject = projectService.activateProject(projectId, ownerId);

        assertEquals(ProjectStatus.ACTIVE, activatedProject.getStatus());
        verify(auditLogPort).register(eq("PROJECT_ACTIVATED"), eq(projectId));
        verify(notificationPort).notify(contains("activated"));
    }

    @Test
    void ActivateProject_WithoutTasks_ShouldFail() {
        UUID ownerId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();
        Project project = new Project(projectId, ownerId, "Test Project");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.findByProjectId(projectId)).thenReturn(Collections.emptyList());

        assertThrows(IllegalStateException.class, () -> projectService.activateProject(projectId, ownerId));
    }

    @Test
    void ActivateProject_ByNonOwner_ShouldFail() {
        UUID ownerId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();
        Project project = new Project(projectId, ownerId, "Test Project");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        assertThrows(SecurityException.class, () -> projectService.activateProject(projectId, otherUserId));
    }
}
