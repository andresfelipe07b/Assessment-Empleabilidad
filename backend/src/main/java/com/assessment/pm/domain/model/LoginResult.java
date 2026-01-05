package com.assessment.pm.domain.model;

public class LoginResult {
    private final String token;
    private final String username;

    public LoginResult(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }
}
