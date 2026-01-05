package com.assessment.pm.domain.ports.out;

import java.util.Optional;
import java.util.UUID;
import com.assessment.pm.domain.model.User;

public interface UserRepositoryPort {
    User save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);
}
