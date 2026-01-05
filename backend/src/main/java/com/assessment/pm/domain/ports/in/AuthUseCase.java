package com.assessment.pm.domain.ports.in;

import com.assessment.pm.domain.model.LoginResult;
import com.assessment.pm.domain.model.User;

public interface AuthUseCase {
    User register(String username, String email, String password);

    LoginResult login(String email, String password);
}
