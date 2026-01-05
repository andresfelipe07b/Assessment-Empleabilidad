package com.assessment.pm.infrastructure.adapter.input.rest;

import com.assessment.pm.domain.model.Task;
import com.assessment.pm.domain.ports.in.CompleteTaskUseCase;
import com.assessment.pm.domain.ports.in.CreateTaskUseCase;
import com.assessment.pm.domain.ports.in.ListTasksUseCase;
import com.assessment.pm.domain.ports.out.CurrentUserPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final CompleteTaskUseCase completeTaskUseCase;
    private final ListTasksUseCase listTasksUseCase;
    private final com.assessment.pm.domain.ports.in.DeleteTaskUseCase deleteTaskUseCase;
    private final CurrentUserPort currentUserPort;

    public TaskController(CreateTaskUseCase createTaskUseCase,
            CompleteTaskUseCase completeTaskUseCase,
            ListTasksUseCase listTasksUseCase,
            com.assessment.pm.domain.ports.in.DeleteTaskUseCase deleteTaskUseCase,
            CurrentUserPort currentUserPort) {
        this.createTaskUseCase = createTaskUseCase;
        this.completeTaskUseCase = completeTaskUseCase;
        this.listTasksUseCase = listTasksUseCase;
        this.deleteTaskUseCase = deleteTaskUseCase;
        this.currentUserPort = currentUserPort;
    }

    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<Task> createTask(@PathVariable UUID projectId, @RequestBody CreateTaskRequest request) {
        UUID ownerId = currentUserPort.getCurrentUserId();
        Task task = createTaskUseCase.createTask(request.title, projectId, ownerId);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<List<Task>> listTasks(@PathVariable UUID projectId) {
        UUID ownerId = currentUserPort.getCurrentUserId();
        List<Task> tasks = listTasksUseCase.listTasks(projectId, ownerId);
        return ResponseEntity.ok(tasks);
    }

    @PatchMapping("/tasks/{id}/complete")
    public ResponseEntity<Task> completeTask(@PathVariable UUID id) {
        UUID ownerId = currentUserPort.getCurrentUserId();
        Task task = completeTaskUseCase.completeTask(id, ownerId);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        UUID ownerId = currentUserPort.getCurrentUserId();
        deleteTaskUseCase.deleteTask(id, ownerId);
        return ResponseEntity.noContent().build();
    }

    public static class CreateTaskRequest {
        public String title;
    }
}
