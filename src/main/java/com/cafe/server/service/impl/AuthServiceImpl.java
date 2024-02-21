package com.cafe.server.service.impl;

import com.cafe.entity.Role;
import com.cafe.entity.User;
import com.cafe.model.dto.security.*;
import com.cafe.server.exception.AlreadyExistsException;
import com.cafe.server.repository.RoleRepository;
import com.cafe.server.repository.UserRepository;
import com.cafe.server.security.jwt.JwtProvider;
import com.cafe.server.security.principle.UserPrinciple;
import com.cafe.server.service.AuthService;
import com.cafe.server.helper.ultis.AlgorithmsUtils;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationmanager;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse handleLogin(LoginRequest request) {
        Authentication authentication = authenticationmanager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        RefreshToken refreshToken = jwtProvider.generateRefreshToken(request.getUsername());
        UserPrinciple user = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return LoginResponse.builder()
                .account(UserDTO.builder().username(user.getUsername()).fullName(user.getFullName()).image(user.getImage()).address(user.getAddress()).roles(roles).build())
                .accessToken(token)
                .refreshToken(refreshToken.getRefreshToken())
                .refreshTokenExpire(refreshToken.getRefreshTokenExpire())
                .build();
    }

    @Override
    @Transactional
    public void registerUser(RegisterRequest request) {

        if (userRepository.existsByUsernameIsOrEmailIs(request.getUsername(), request.getEmail())) {
            throw new AlreadyExistsException("Email or username is exists");
        }
        List<Role> roles = roleRepository.findByRoleNameIn(request.getRoles());

        User user = User.builder().fullName(request.getFullName()).username(request.getUsername()).email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
                .roles(roles).build();
        userRepository.save(user);
    }


}
