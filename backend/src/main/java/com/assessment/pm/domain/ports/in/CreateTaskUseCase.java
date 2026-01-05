package com.assessment.pm.domain.ports.in;

import com.assessment.pm.domain.model.Task;
import java.util.UUID;

public interface CreateTaskUseCase {
    Task createTask(String title, UUID projectId, UUID ownerId);
}
