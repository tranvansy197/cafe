package com.cafe.model.dto.security;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RefreshToken {
    private String refreshToken;
    private LocalDateTime refreshTokenExpire;
}
