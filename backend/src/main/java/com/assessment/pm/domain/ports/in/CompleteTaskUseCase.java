package com.assessment.pm.domain.ports.in;

import com.assessment.pm.domain.model.Task;
import java.util.UUID;

public interface CompleteTaskUseCase {
    Task completeTask(UUID taskId, UUID ownerId);
}
