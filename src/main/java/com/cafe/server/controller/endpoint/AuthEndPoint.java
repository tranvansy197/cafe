package com.cafe.server.controller.endpoint;

import com.cafe.model.dto.security.LoginRequest;
import com.cafe.model.dto.security.LoginResponse;
import com.cafe.model.dto.security.RegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface AuthEndPoint {
    @PostMapping("/login")
    @ResponseStatus(value = HttpStatus.OK)
    LoginResponse login(LoginRequest request);

    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.OK)
    void register(RegisterRequest request);
}
