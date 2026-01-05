package com.assessment.pm.infrastructure.adapter.output.security;

import com.assessment.pm.domain.ports.out.CurrentUserPort;
import com.assessment.pm.domain.ports.out.PasswordEncoderPort;
import com.assessment.pm.domain.ports.out.TokenProviderPort;
import com.assessment.pm.infrastructure.security.CustomUserDetails;
import com.assessment.pm.infrastructure.security.JwtUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SpringSecurityAdapter implements PasswordEncoderPort, TokenProviderPort, CurrentUserPort {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public SpringSecurityAdapter(PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public String generateToken(UUID userId, String email) {
        return jwtUtils.generateToken(userId, email);
    }

    @Override
    public UUID getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getId();
        }
        throw new IllegalStateException("User authenticated but principal is not CustomUserDetails");
    }
}
