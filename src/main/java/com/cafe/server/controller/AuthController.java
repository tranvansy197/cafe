package com.cafe.server.controller;

import com.cafe.model.constant.APIConstant;
import com.cafe.model.dto.security.LoginRequest;
import com.cafe.model.dto.security.LoginResponse;
import com.cafe.model.dto.security.RegisterRequest;
import com.cafe.server.controller.endpoint.AuthEndPoint;
import com.cafe.server.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIConstant.API_URL)
@RequiredArgsConstructor
public class AuthController implements AuthEndPoint {
    private final AuthService authService;
    @Override
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.handleLogin(request);
    }

    @Override
    public void register(@Valid @RequestBody RegisterRequest request) {
         authService.registerUser(request);
    }
}
