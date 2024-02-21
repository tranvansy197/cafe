package com.cafe.model.dto.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class LoginResponse {
    private UserDTO account;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime refreshTokenExpire;
}
