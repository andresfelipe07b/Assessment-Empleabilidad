package com.assessment.pm.domain.model;

import java.util.UUID;

public class Task {
    private UUID id;
    private UUID projectId;
    private String title;
    private boolean completed;
    private boolean deleted;

    public Task(UUID id, UUID projectId, String title) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.completed = false;
        this.deleted = false;
    }

    public Task(UUID id, UUID projectId, String title, boolean completed, boolean deleted) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.completed = completed;
        this.deleted = deleted;
    }

    public void complete() {
        if (this.completed) {
            throw new IllegalStateException("Task is already completed.");
        }
        this.completed = true;
    }

    public void delete() {
        this.deleted = true;
    }

    public boolean isCompleted() {
        return completed;
    }

    public UUID getId() {
        return id;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
