package com.assessment.pm.domain.ports.in;

import java.util.UUID;

public interface DeleteTaskUseCase {
    void deleteTask(UUID taskId, UUID ownerId);
}
