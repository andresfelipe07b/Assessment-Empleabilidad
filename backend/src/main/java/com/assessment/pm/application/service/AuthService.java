package com.assessment.pm.application.service;

import com.assessment.pm.domain.model.LoginResult;
import com.assessment.pm.domain.model.User;
import com.assessment.pm.domain.ports.in.AuthUseCase;
import com.assessment.pm.domain.ports.out.PasswordEncoderPort;
import com.assessment.pm.domain.ports.out.TokenProviderPort;
import com.assessment.pm.domain.ports.out.UserRepositoryPort;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AuthService implements AuthUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final TokenProviderPort tokenProvider;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    public AuthService(UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder,
            TokenProviderPort tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public User register(String username, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(UUID.randomUUID(), username, email, encodedPassword);
        return userRepository.save(user);
    }

    @Override
    public LoginResult login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = tokenProvider.generateToken(user.getId(), user.getEmail());
        return new LoginResult(token, user.getUsername());
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }
}
