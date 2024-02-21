package com.cafe.server.service;

import com.cafe.model.dto.security.LoginRequest;
import com.cafe.model.dto.security.LoginResponse;
import com.cafe.model.dto.security.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    LoginResponse handleLogin(LoginRequest request);

    void registerUser(RegisterRequest request);
}
