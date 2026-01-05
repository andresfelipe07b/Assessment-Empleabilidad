package com.assessment.pm.domain.ports.out;

import java.util.UUID;

public interface TokenProviderPort {
    String generateToken(UUID userId, String email);
}
