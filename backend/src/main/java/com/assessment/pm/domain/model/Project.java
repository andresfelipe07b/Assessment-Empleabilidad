package com.assessment.pm.domain.model;

import java.util.UUID;
import java.util.List;

public class Project {
    private UUID id;
    private UUID ownerId;
    private String name;
    private ProjectStatus status;
    private boolean deleted;

    public Project(UUID id, UUID ownerId, String name) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.status = ProjectStatus.DRAFT;
        this.deleted = false;
    }

    // Constructor for reconstitution
    public Project(UUID id, UUID ownerId, String name, ProjectStatus status, boolean deleted) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.status = status;
        this.deleted = deleted;
    }

    public void activate(List<Task> tasks) {
        if (!canBeActivated(tasks)) {
            throw new IllegalStateException("Project cannot be activated. It requires at least one active task.");
        }
        this.status = ProjectStatus.ACTIVE;
    }

    public boolean canBeActivated(List<Task> tasks) {
        return tasks != null && tasks.stream().anyMatch(t -> !t.isCompleted() && !t.isDeleted());
    }

    public boolean isOwner(UUID userId) {
        return this.ownerId.equals(userId);
    }
    
    public ProjectStatus getStatus() {
        return status;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
