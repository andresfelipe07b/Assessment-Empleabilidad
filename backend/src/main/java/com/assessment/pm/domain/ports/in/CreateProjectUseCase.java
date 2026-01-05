package com.assessment.pm.domain.ports.in;

import com.assessment.pm.domain.model.Project;
import java.util.UUID;

public interface CreateProjectUseCase {
    Project createProject(String name, UUID ownerId);
}
