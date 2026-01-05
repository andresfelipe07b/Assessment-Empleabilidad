package com.assessment.pm.domain.ports.in;

import com.assessment.pm.domain.model.Task;
import java.util.List;
import java.util.UUID;

public interface ListTasksUseCase {
    List<Task> listTasks(UUID projectId, UUID ownerId);
}
