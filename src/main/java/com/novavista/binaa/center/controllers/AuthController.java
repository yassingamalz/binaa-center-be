package com.novavista.binaa.center.controllers;

import com.novavista.binaa.center.dto.LoginRequest;
import com.novavista.binaa.center.dto.LoginResponse;
import com.novavista.binaa.center.dto.UserDTO;
import com.novavista.binaa.center.entity.User;
import com.novavista.binaa.center.enums.UserRole;
import com.novavista.binaa.center.security.CustomUserDetails;
import com.novavista.binaa.center.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.debug("Login attempt for username: {}", loginRequest.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = ((CustomUserDetails) userDetails).getUser();

            // Create UserDTO with required information
            UserDTO userDTO = UserDTO.builder()
                    .userId(user.getUserId())
                    .username(user.getUsername())
                    .role(UserRole.valueOf(user.getRole().name()))
                    .build();

            // Generate token with user information
            String jwt = tokenProvider.generateToken(authentication);

            log.debug("Login successful for user: {}", loginRequest.getUsername());
            return ResponseEntity.ok(new LoginResponse(jwt, userDTO));

        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", loginRequest.getUsername(), e);
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
    }
}