package com.cafe.server.security.jwt;

import com.cafe.entity.User;
import com.cafe.model.dto.security.RefreshToken;
import com.cafe.server.repository.UserRepository;
import com.cafe.server.security.principle.UserPrinciple;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Component
@Log4j2
public class JwtProvider {
    private final String key;
    private final Integer tokenExpire;
    private final Integer refreshTokenExpire;
    private final String algorithm;

    private final UserRepository userRepository;

    public JwtProvider(@Value("${server.security.token-expire}")String tokenExpire,
                       @Value("${server.security.refresh-token-expire}")String refreshTokenExpire,
                       @Value("${server.security.algorithm}")String algorithm,
                       @Value("${server.security.key}")String key,
                       UserRepository userRepository) {
        this.algorithm = algorithm;
        this.key = key;
        this.tokenExpire = Integer.parseInt(tokenExpire);
        this.refreshTokenExpire = Integer.parseInt(refreshTokenExpire);
        this.userRepository = userRepository;
    }

    public String generateToken(Authentication authentication) {
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        return Jwts.builder().setSubject(userPrinciple.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + tokenExpire * 1000))
                .signWith(SignatureAlgorithm.valueOf(algorithm), key)
                .compact();
    }

    public RefreshToken generateRefreshToken(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
        String refreshToken = String.valueOf(UUID.randomUUID());
        LocalDateTime expirationDateTime = LocalDateTime.now().plusDays(refreshTokenExpire);
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpire(expirationDateTime);
        userRepository.save(user);
        return RefreshToken.builder().refreshToken(refreshToken).refreshTokenExpire(expirationDateTime).build();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature -> Message: {}", e);
        } catch (MalformedJwtException e) {
            log.error("The token invalid format -> Message: {}");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            log.error("Jwt claims string is empty -> Message: {}", e);
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
    }
}
