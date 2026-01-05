package com.assessment.pm.infrastructure.adapter.input.rest;

import com.assessment.pm.domain.model.Project;
import com.assessment.pm.domain.ports.in.ActivateProjectUseCase;
import com.assessment.pm.domain.ports.in.CreateProjectUseCase;
import com.assessment.pm.domain.ports.in.ListProjectsUseCase;
import com.assessment.pm.domain.ports.out.CurrentUserPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final CreateProjectUseCase createProjectUseCase;
    private final ActivateProjectUseCase activateProjectUseCase;
    private final ListProjectsUseCase listProjectsUseCase;
    private final CurrentUserPort currentUserPort;

    public ProjectController(CreateProjectUseCase createProjectUseCase,
            ActivateProjectUseCase activateProjectUseCase,
            ListProjectsUseCase listProjectsUseCase,
            CurrentUserPort currentUserPort) {
        this.createProjectUseCase = createProjectUseCase;
        this.activateProjectUseCase = activateProjectUseCase;
        this.listProjectsUseCase = listProjectsUseCase;
        this.currentUserPort = currentUserPort;
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody CreateProjectRequest request) {
        UUID ownerId = currentUserPort.getCurrentUserId();
        Project project = createProjectUseCase.createProject(request.name, ownerId);
        return ResponseEntity.ok(project);
    }

    @GetMapping
    public ResponseEntity<List<Project>> listProjects() {
        UUID ownerId = currentUserPort.getCurrentUserId();
        List<Project> projects = listProjectsUseCase.listProjects(ownerId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable UUID id) {
        UUID ownerId = currentUserPort.getCurrentUserId();
        Project project = listProjectsUseCase.getProject(id, ownerId);
        return ResponseEntity.ok(project);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Project> activateProject(@PathVariable UUID id) {
        UUID ownerId = currentUserPort.getCurrentUserId();
        Project project = activateProjectUseCase.activateProject(id, ownerId);
        return ResponseEntity.ok(project);
    }

    public static class CreateProjectRequest {
        public String name;
    }
}
