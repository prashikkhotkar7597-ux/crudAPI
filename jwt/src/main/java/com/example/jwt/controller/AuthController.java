package com.example.jwt.controller;

import com.example.jwt.model.AuthRequest;
import com.example.jwt.model.User;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.util.JwtUtil;

import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authManager,
                          JwtUtil jwtUtil,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 🔐 REGISTER USER
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {

        Map<String, Object> response = new HashMap<>();

        // check if user already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            response.put("status", "error");
            response.put("message", "Username already exists");
            return response;
        }

        // encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        response.put("status", "success");
        response.put("message", "User registered successfully");

        return response;
    }

    // 🔑 LOGIN USER
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody AuthRequest request) {

        Map<String, Object> response = new HashMap<>();

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword())
            );

            String token = jwtUtil.generateToken(request.getUsername());

            response.put("status", "success");
            response.put("token", token);

        } catch (AuthenticationException e) {
            response.put("status", "error");
            response.put("message", "Invalid username or password");
        }

        return response;
    }
}