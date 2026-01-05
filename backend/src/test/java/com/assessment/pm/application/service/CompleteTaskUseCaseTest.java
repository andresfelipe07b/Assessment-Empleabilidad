package com.assessment.pm.application.service;

import com.assessment.pm.domain.model.Project;
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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompleteTaskUseCaseTest {

    @Mock
    private TaskRepositoryPort taskRepository;
    @Mock
    private ProjectRepositoryPort projectRepository;
    @Mock
    private NotificationPort notificationPort;
    @Mock
    private AuditLogPort auditLogPort;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository, projectRepository, notificationPort, auditLogPort);
    }

    @Test
    void CompleteTask_ShouldGenerateAuditAndNotification() {
        UUID ownerId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();
        Project project = new Project(projectId, ownerId, "Test Project");
        Task task = new Task(taskId, projectId, "Task 1");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArguments()[0]);

        taskService.completeTask(taskId, ownerId);

        assertTrue(task.isCompleted());
        verify(auditLogPort).register(eq("TASK_COMPLETED"), eq(taskId));
        verify(notificationPort).notify(contains("completed"));
    }

    @Test
    void CompleteTask_AlreadyCompleted_ShouldFail() {
        UUID ownerId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();
        Project project = new Project(projectId, ownerId, "Test Project");
        Task task = new Task(taskId, projectId, "Task 1", true, false);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        assertThrows(IllegalStateException.class, () -> taskService.completeTask(taskId, ownerId));
    }
}
