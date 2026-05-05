package com.example.jwt.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    // ✅ Simple protected API
    @GetMapping("/hello")
    public String hello() {
        return "JWT Working Successfully!";
    }

    // 🔐 Get logged-in user details from JWT
    @GetMapping("/profile")
    public Map<String, Object> getProfile(Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        response.put("username", authentication.getName());
        response.put("message", "User profile fetched successfully");

        return response;
    }

    // 🔐 Example POST API (secured)
    @PostMapping("/data")
    public Map<String, Object> saveData(@RequestBody Map<String, Object> data,
                                        Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        response.put("username", authentication.getName());
        response.put("receivedData", data);
        response.put("message", "Data saved successfully");

        return response;
    }
}