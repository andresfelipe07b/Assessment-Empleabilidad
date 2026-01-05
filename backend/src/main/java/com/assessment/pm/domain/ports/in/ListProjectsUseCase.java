package com.assessment.pm.domain.ports.in;

import com.assessment.pm.domain.model.Project;
import java.util.List;
import java.util.UUID;

public interface ListProjectsUseCase {
    List<Project> listProjects(UUID ownerId);

    Project getProject(UUID projectId, UUID ownerId);
}
